<<<<<<< main
# Payflex Customer Portal - Automated Login Test

This project automates the login process for the [Payflex UAT Customer Portal](https://customer.uat.payflex.co.za/) using Selenium, Java, and Cucumber. It performs login via API and also the UI, injects the token into the browser, and then navigates directly to the UI.

---

## ✅ Features

- Login using **Auth0 API** to fetch a bearer token
- Token injected via **JavaScript/localStorage** to simulate session
- UI then continues from the **Dashboard** page
- Uses **Page Object Model**
- Step Definitions with **Cucumber BDD**
- External **Test Data** in `testdata.properties`

---

## 🚧 Known Issues / Blockers

### 🧩 Dashboard Icon Element Not Locatable

During test execution, an issue was encountered when trying to locate the dashboard icon after login.

- **Problem:** The dashboard icon element does not load immediately or is rendered via JavaScript asynchronously.
- **Symptoms:**
    - `NoSuchElementException` or timeout when trying to locate the dashboard link/icon.
- **Possible Causes:**
    - Element loads dynamically after token validation
    - Shadow DOM or iframes involved (needs inspection)

-----
