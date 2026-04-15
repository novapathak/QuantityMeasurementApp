const state = {
    catalog: null,
    token: localStorage.getItem("qm_token") || "",
    toastTimer: null
};

const arithmeticOperations = new Set(["ADD", "SUBTRACT", "MULTIPLY", "DIVIDE"]);

const elements = {
    loginForm: document.getElementById("loginForm"),
    registerForm: document.getElementById("registerForm"),
    operationForm: document.getElementById("operationForm"),
    measurementType: document.getElementById("measurementType"),
    operationType: document.getElementById("operationType"),
    firstValue: document.getElementById("firstValue"),
    firstUnit: document.getElementById("firstUnit"),
    secondValue: document.getElementById("secondValue"),
    secondUnit: document.getElementById("secondUnit"),
    targetUnit: document.getElementById("targetUnit"),
    secondValueField: document.getElementById("secondValueField"),
    secondUnitField: document.getElementById("secondUnitField"),
    targetUnitField: document.getElementById("targetUnitField"),
    authStateBadge: document.getElementById("authStateBadge"),
    measurementHint: document.getElementById("measurementHint"),
    resultHeadline: document.getElementById("resultHeadline"),
    resultSummary: document.getElementById("resultSummary"),
    historyOperation: document.getElementById("historyOperation"),
    historyMeasurementType: document.getElementById("historyMeasurementType"),
    loadOperationHistoryButton: document.getElementById("loadOperationHistoryButton"),
    loadOperationCountButton: document.getElementById("loadOperationCountButton"),
    loadTypeHistoryButton: document.getElementById("loadTypeHistoryButton"),
    loadErroredButton: document.getElementById("loadErroredButton"),
    historyTableBody: document.getElementById("historyTableBody"),
    apiConsole: document.getElementById("apiConsole"),
    consoleStatus: document.getElementById("consoleStatus"),
    refreshSessionButton: document.getElementById("refreshSessionButton"),
    logoutButton: document.getElementById("logoutButton"),
    profileCard: document.getElementById("profileCard"),
    profileName: document.getElementById("profileName"),
    profileUsername: document.getElementById("profileUsername"),
    profileEmail: document.getElementById("profileEmail"),
    profileProvider: document.getElementById("profileProvider"),
    profileRoles: document.getElementById("profileRoles"),
    googleLoginLink: document.getElementById("googleLoginLink"),
    toast: document.getElementById("toast")
};

document.addEventListener("DOMContentLoaded", init);

async function init() {
    bindEvents();
    setConnectedState(Boolean(state.token));

    try {
        await loadCatalog();
        if (state.token) {
            await loadProfile(false);
        }
    } catch (error) {
        showToast(error.message || "Unable to initialize the app.");
        logResponse("Initialization error", { message: error.message });
    }
}

function bindEvents() {
    elements.loginForm.addEventListener("submit", handleLogin);
    elements.registerForm.addEventListener("submit", handleRegister);
    elements.operationForm.addEventListener("submit", handleOperation);
    elements.measurementType.addEventListener("change", syncOperationForm);
    elements.operationType.addEventListener("change", syncOperationForm);
    elements.firstUnit.addEventListener("change", syncOperationForm);
    elements.refreshSessionButton.addEventListener("click", () => runTask(() => loadProfile(true)));
    elements.logoutButton.addEventListener("click", clearSession);
    elements.loadOperationHistoryButton.addEventListener("click", () => runTask(() => loadOperationHistory(elements.historyOperation.value)));
    elements.loadOperationCountButton.addEventListener("click", () => runTask(() => loadOperationCount(elements.historyOperation.value)));
    elements.loadTypeHistoryButton.addEventListener("click", () => runTask(() => loadTypeHistory(elements.historyMeasurementType.value)));
    elements.loadErroredButton.addEventListener("click", () => runTask(loadErroredHistory));
}

async function loadCatalog() {
    const catalog = await requestJson("/api/v1/metadata/measurements", { method: "GET" }, false);
    state.catalog = catalog;

    populateSelect(
        elements.measurementType,
        catalog.measurementTypes.map((type) => ({ value: type.name, label: type.label }))
    );
    populateSelect(
        elements.historyMeasurementType,
        catalog.measurementTypes.map((type) => ({ value: type.name, label: type.label }))
    );
    populateSelect(
        elements.historyOperation,
        catalog.operations.map((operation) => ({ value: operation.name, label: operation.label }))
    );

    renderOperationOptions();
    elements.googleLoginLink.classList.toggle("hidden", !catalog.googleOAuthEnabled);
    syncOperationForm();
}

function renderOperationOptions() {
    const selectedType = getSelectedMeasurementType();
    const previousValue = elements.operationType.value;

    elements.operationType.innerHTML = "";
    state.catalog.operations.forEach((operation) => {
        const option = document.createElement("option");
        option.value = operation.name;
        option.textContent = operation.label;
        option.disabled = !selectedType.arithmeticSupported && arithmeticOperations.has(operation.name);
        elements.operationType.appendChild(option);
    });

    const reusableValue = Array.from(elements.operationType.options).some((option) => option.value === previousValue && !option.disabled)
        ? previousValue
        : Array.from(elements.operationType.options).find((option) => !option.disabled)?.value;

    if (reusableValue) {
        elements.operationType.value = reusableValue;
    }
}

function syncOperationForm() {
    if (!state.catalog) {
        return;
    }

    renderOperationOptions();

    const measurementType = getSelectedMeasurementType();
    const operation = getSelectedOperation();
    const units = measurementType.units;
    const firstUnitValue = pickAvailableValue(elements.firstUnit.value, units, units[0]);
    const secondUnitDefault = pickAvailableValue(elements.secondUnit.value, units, units[Math.min(1, units.length - 1)] || units[0]);
    const targetUnitDefault = pickAvailableValue(elements.targetUnit.value, units, units[0]);

    populateSelect(elements.firstUnit, units.map(asOption), firstUnitValue);
    populateSelect(elements.secondUnit, units.map(asOption), secondUnitDefault);
    populateSelect(elements.targetUnit, units.map(asOption), targetUnitDefault);

    elements.secondValueField.classList.toggle("hidden", !operation.requiresSecondQuantity);
    elements.secondUnitField.classList.toggle("hidden", !operation.requiresSecondQuantity);
    elements.targetUnitField.classList.toggle("hidden", !operation.allowsTargetUnit);
    elements.secondValue.required = operation.requiresSecondQuantity;
    elements.secondUnit.required = operation.requiresSecondQuantity;
    elements.targetUnit.required = operation.requiresTargetUnit;

    if (!measurementType.arithmeticSupported) {
        elements.measurementHint.textContent = "Temperature supports convert and compare only.";
    } else {
        elements.measurementHint.textContent = `${measurementType.label} units are ready for arithmetic and conversion.`;
    }
}

async function handleLogin(event) {
    event.preventDefault();
    try {
        const formData = new FormData(elements.loginForm);
        const payload = Object.fromEntries(formData.entries());
        const response = await requestJson("/api/v1/auth/login", jsonRequest(payload), false);
        persistSession(response);
        elements.loginForm.reset();
        elements.loginForm.username.value = response.username;
        showToast("Login successful.");
        setResult("Authenticated successfully.", `${response.username} can now call protected backend endpoints.`);
    } catch (error) {
        showToast(error.message);
    }
}

async function handleRegister(event) {
    event.preventDefault();
    try {
        const formData = new FormData(elements.registerForm);
        const payload = Object.fromEntries(formData.entries());
        const response = await requestJson("/api/v1/auth/register", jsonRequest(payload), false);
        persistSession(response);
        elements.registerForm.reset();
        showToast("Account created and authenticated.");
        setResult("Registration completed.", `${response.username} is now signed in with a JWT from the backend.`);
    } catch (error) {
        showToast(error.message);
    }
}

async function handleOperation(event) {
    event.preventDefault();
    try {
        ensureAuthenticated();

        const measurementType = elements.measurementType.value;
        const operation = getSelectedOperation();
        const payload = {
            thisQuantityDTO: {
                value: Number(elements.firstValue.value),
                unit: elements.firstUnit.value,
                measurementType
            }
        };

        if (operation.requiresSecondQuantity) {
            payload.thatQuantityDTO = {
                value: Number(elements.secondValue.value),
                unit: elements.secondUnit.value,
                measurementType
            };
        }

        const query = new URLSearchParams();
        if (operation.allowsTargetUnit && elements.targetUnit.value) {
            query.set("targetUnit", elements.targetUnit.value);
        }

        const path = `/api/v1/quantities/${operation.name.toLowerCase()}${query.toString() ? `?${query.toString()}` : ""}`;
        const response = await requestJson(path, jsonRequest(payload), true);

        const summary = formatQuantityResponse(response);
        setResult(`${operation.label} completed.`, summary);
    } catch (error) {
        showToast(error.message);
    }
}

async function loadProfile(showToastMessage) {
    if (!state.token) {
        clearProfile();
        if (showToastMessage) {
            showToast("No active session.");
        }
        return;
    }

    try {
        const response = await requestJson("/api/v1/auth/me", { method: "GET" }, true);
        renderProfile(response);
        setConnectedState(true);
        if (showToastMessage) {
            showToast("Session refreshed.");
        }
    } catch (error) {
        clearSession();
        throw error;
    }
}

async function loadOperationHistory(operationName) {
    ensureAuthenticated();
    const response = await requestJson(`/api/v1/quantities/history/operation/${encodeURIComponent(operationName)}`, { method: "GET" }, true);
    renderHistory(response, `${operationName} history`);
}

async function loadTypeHistory(measurementType) {
    ensureAuthenticated();
    const response = await requestJson(`/api/v1/quantities/history/type/${encodeURIComponent(measurementType)}`, { method: "GET" }, true);
    renderHistory(response, `${measurementType} history`);
}

async function loadErroredHistory() {
    ensureAuthenticated();
    const response = await requestJson("/api/v1/quantities/history/errored", { method: "GET" }, true);
    renderHistory(response, "Errored history");
}

async function loadOperationCount(operationName) {
    ensureAuthenticated();
    const response = await requestJson(`/api/v1/quantities/count/${encodeURIComponent(operationName)}`, { method: "GET" }, true);
    const total = typeof response === "number" ? response : Number(response);
    setResult("Operation count loaded.", `${operationName} succeeded ${total} time(s).`);
}

function renderHistory(entries, label) {
    elements.historyTableBody.innerHTML = "";

    if (!Array.isArray(entries) || entries.length === 0) {
        const row = document.createElement("tr");
        row.innerHTML = `<td colspan="4" class="empty-state">No entries returned for ${escapeHtml(label)}.</td>`;
        elements.historyTableBody.appendChild(row);
        setResult("History loaded.", `No records returned for ${label}.`);
        return;
    }

    entries.forEach((entry) => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${escapeHtml(entry.operation || "-")}</td>
            <td>${escapeHtml(formatInputSummary(entry))}</td>
            <td>${escapeHtml(formatResultSummary(entry))}</td>
            <td>${entry.error ? "Errored" : "Successful"}</td>
        `;
        elements.historyTableBody.appendChild(row);
    });

    setResult("History loaded.", `${entries.length} record(s) returned for ${label}.`);
}

function persistSession(authResponse) {
    state.token = authResponse.accessToken || "";
    localStorage.setItem("qm_token", state.token);
    setConnectedState(true);
    renderProfile(authResponse);
}

function clearSession() {
    state.token = "";
    localStorage.removeItem("qm_token");
    setConnectedState(false);
    clearProfile();
    showToast("Session cleared.");
    logResponse("Session cleared", {});
}

function clearProfile() {
    elements.profileCard.classList.add("hidden");
    elements.profileName.textContent = "Not signed in";
    elements.profileUsername.textContent = "-";
    elements.profileEmail.textContent = "-";
    elements.profileProvider.textContent = "-";
    elements.profileRoles.textContent = "-";
}

function renderProfile(profile) {
    elements.profileCard.classList.remove("hidden");
    elements.profileName.textContent = profile.fullName || profile.username || "Authenticated User";
    elements.profileUsername.textContent = profile.username || "-";
    elements.profileEmail.textContent = profile.email || "-";
    elements.profileProvider.textContent = profile.authProvider || "-";
    elements.profileRoles.textContent = Array.isArray(profile.authorities) && profile.authorities.length
        ? profile.authorities.join(", ")
        : "-";
}

function setConnectedState(isConnected) {
    elements.authStateBadge.textContent = isConnected ? "Connected" : "Disconnected";
    elements.authStateBadge.classList.toggle("muted", !isConnected);
}

function setResult(headline, summary) {
    elements.resultHeadline.textContent = headline;
    elements.resultSummary.textContent = summary;
}

function ensureAuthenticated() {
    if (!state.token) {
        throw new Error("Login or register before calling protected quantity endpoints.");
    }
}

async function requestJson(path, options, authenticated) {
    const requestOptions = {
        method: options.method || "GET",
        headers: new Headers(options.headers || {})
    };

    if (options.body !== undefined) {
        requestOptions.body = options.body;
    }

    if (authenticated) {
        requestOptions.headers.set("Authorization", `Bearer ${state.token}`);
    }

    const response = await fetch(path, requestOptions);
    const contentType = response.headers.get("content-type") || "";
    const body = contentType.includes("application/json")
        ? await response.json()
        : await response.text();

    logResponse(`${requestOptions.method} ${path}`, body);

    if (!response.ok) {
        const message = typeof body === "string"
            ? body
            : body.message || body.error || "The backend returned an error.";
        throw new Error(message);
    }

    return body;
}

function jsonRequest(payload) {
    return {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(payload)
    };
}

function populateSelect(select, options, selectedValue) {
    select.innerHTML = "";
    options.forEach((optionData) => {
        const option = document.createElement("option");
        option.value = optionData.value;
        option.textContent = optionData.label;
        select.appendChild(option);
    });

    if (selectedValue) {
        select.value = selectedValue;
    }
}

function getSelectedMeasurementType() {
    return state.catalog.measurementTypes.find((type) => type.name === elements.measurementType.value) || state.catalog.measurementTypes[0];
}

function getSelectedOperation() {
    return state.catalog.operations.find((operation) => operation.name === elements.operationType.value) || state.catalog.operations[0];
}

function pickAvailableValue(currentValue, allowedValues, fallbackValue) {
    return allowedValues.includes(currentValue) ? currentValue : fallbackValue;
}

function asOption(value) {
    return { value, label: value };
}

function formatQuantityResponse(response) {
    if (response.error) {
        return response.errorMessage || "The backend marked this request as an error.";
    }
    return formatResultSummary(response);
}

function formatInputSummary(entry) {
    const left = `${entry.thisValue ?? "-"} ${entry.thisUnit || ""}`.trim();
    const right = entry.thatValue != null && entry.thatUnit ? `${entry.thatValue} ${entry.thatUnit}` : "n/a";
    return `${left} vs ${right}`;
}

function formatResultSummary(entry) {
    if (entry.resultString) {
        return entry.resultString;
    }
    if (entry.resultValue != null && entry.resultUnit) {
        return `${entry.resultValue} ${entry.resultUnit}`;
    }
    if (entry.resultValue != null) {
        return String(entry.resultValue);
    }
    return entry.errorMessage || "No result payload";
}

function logResponse(label, body) {
    elements.consoleStatus.textContent = label;
    elements.apiConsole.textContent = typeof body === "string" ? body : JSON.stringify(body, null, 2);
}

function showToast(message) {
    clearTimeout(state.toastTimer);
    elements.toast.textContent = message;
    elements.toast.classList.add("visible");
    state.toastTimer = window.setTimeout(() => {
        elements.toast.classList.remove("visible");
    }, 2600);
}

function runTask(task) {
    Promise.resolve()
        .then(task)
        .catch((error) => {
            showToast(error.message || "Request failed.");
        });
}

function escapeHtml(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll("\"", "&quot;")
        .replaceAll("'", "&#39;");
}
