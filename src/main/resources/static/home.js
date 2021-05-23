$(document).ready(() => {
    const $usernameInput = $("#username-input");
    const $passwordInput = $("#password-input");
    const $accountIdCreateInput = $("#account-id-create-input");
    const $urlInput = $("#url-input");
    const $redirectTypeInput = $("#redirect-type-input");
    const $accountIdGetInput = $("#account-id-get-input");

    function adaptError(xhr) {
        try {
            return JSON.parse(xhr.responseText);
        } catch(e) {
            return "";
        }
    }

    function showResult(data, xhr, success) {
        const content = document.createElement("pre");
        content.innerHTML = prettyJson(data);
        swal({
            title: xhr.status,
            content: content,
            icon: success ? "success" : "error",
            className: "swal-custom"
        });
    }

    $("#btn-create-account").click(() => {
        const accountId = $accountIdCreateInput.val().trim();
        $.ajax({
            type: "POST",
            url: "/account",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({accountId}),
            success: (data, statusText, xhr) => showResult(data, xhr, true),
            error: xhr => showResult(adaptError(xhr), xhr, false)
        });
    });

    $("#btn-create-url-mapping").click(() => {
        const url = $urlInput.val();
        const redirectType = $redirectTypeInput.val();
        const username = $usernameInput.val();
        const password = $passwordInput.val();
        $.ajax({
            type: "POST",
            url: "/register",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({url, redirectType}),
            success: (data, statusText, xhr) => showResult(data, xhr, true),
            error: xhr => showResult(adaptError(xhr), xhr, false),
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));
            },
        });
    });

    $("#btn-get-statistic").click(() => {
        const accountId = $accountIdGetInput.val();
        const username = $usernameInput.val();
        const password = $passwordInput.val();
        $.ajax({
            type: "GET",
            url: `/statistic/${encodeURIComponent(accountId)}`,
            success: (data, statusText, xhr) => showResult(data, xhr, true),
            error: xhr => showResult(adaptError(xhr), xhr, false),
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));
            },
        });
    });
});