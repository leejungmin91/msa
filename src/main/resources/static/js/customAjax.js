var apiPrefix = "/api/v1";
let accessToken = "";

//ajax type setting
const AJAX_TYPE_POST = "POST";
const AJAX_TYPE_GET = "GET";
const AJAX_TYPE_PUT = "PUT";
const AJAX_TYPE_PATCH = "PATCH";
const AJAX_TYPE_DELETE = "DELETE";

$(document).ajaxStart(function () {
    $("#loadingOverlay").show();
});

// 모든 AJAX 요청 끝나면 로딩 오버레이 숨기기
$(document).ajaxStop(function () {
    $("#loadingOverlay").fadeOut(200);
});

//post function
const PostAjax = (url, data, requiresAuth) => {
    return PromiseAjax(url, data, AJAX_TYPE_POST, requiresAuth);
}

//get function
const GetAjax = (url, data, requiresAuth) => {
    return PromiseAjax(url, data, AJAX_TYPE_GET, requiresAuth);
}

const PutAjax = (url, data, requiresAuth) => {
    return PromiseAjax(url, data, AJAX_TYPE_PUT, requiresAuth);
}

const PatchAjax = (url, data, requiresAuth) => {
    return PromiseAjax(url, data, AJAX_TYPE_PATCH, requiresAuth);
}

const DeleteAjax = (url, data, requiresAuth) => {
    return PromiseAjax(url, data, AJAX_TYPE_DELETE, requiresAuth);
}

//ajax main function(promise)
const PromiseAjax = (url, data, type, requiresAuth = false) => {

    return new Promise((resolve, reject) => {
        $.ajax({
            url     : apiPrefix+url,
            data    : (AJAX_TYPE_POST === type || AJAX_TYPE_PATCH === type || AJAX_TYPE_PUT === type ? JSON.stringify ( data ) : data),
            type    : type,
            contentType: "application/json",  // JSON 형식으로 전송
            dataType: "json",
            xhrFields: {
                withCredentials: requiresAuth
            },
            success : function (response) {
                if(response.code && response.code !== "0000"){
                    alert(response.message);
                    return;
                }
                resolve(response.data);
            },
            error: function (xhr) {
                const code = xhr.responseJSON.code;
                const message = xhr.responseJSON.message;
                if(code === '1005') {
                    refreshTokenAjax();
                } else if(code === '1009') {
                    alert(message);
                    window.location.href = "/";
                    return;
                }
                reject(xhr);
            }
        });
    });
}

const refreshTokenAjax = () => {
    return $.ajax({
        url     : apiPrefix+'/auth/reissue',
        type    : AJAX_TYPE_POST,
        contentType: "application/json",  // JSON 형식으로 전송
        dataType: "json",
        xhrFields: {
            withCredentials: true
        },
        success : function (response, status, xhr) {
            location.reload();
        },
        error: function (xhr) {
            if(window.location.pathname !== '/login') {
                window.location.href = "/login";
            }
        }
    });
}


const loginAjax = (data) => {
    return new Promise((resolve) => {
        $.ajax({
            url     : apiPrefix+'/auth/login',
            data    : JSON.stringify ( data ),
            type    : AJAX_TYPE_POST,
            contentType: "application/json",  // JSON 형식으로 전송
            dataType: "json",
            xhrFields: {
                withCredentials: true
            },
            success : function (response, status, xhr) {
                resolve(true);
            },
            error: function (xhr) {
                resolve(false);
            }
        });
    });
}

const logoutAjax = () => {
    return new Promise((resolve) => {
        $.ajax({
            url     : apiPrefix+'/auth/logout',
            type    : AJAX_TYPE_POST,
            contentType: "application/json",  // JSON 형식으로 전송
            dataType: "json",
            xhrFields: {
                withCredentials: true
            },
            success : function (response, status, xhr) {
                resolve(true);
            },
            error: function (xhr) {
                resolve(false);
            }
        });
    });
}

const authCheckAjax = () => {
    return new Promise((resolve) => {
        $.ajax({
            url: apiPrefix + `/auth/check`,
            type: AJAX_TYPE_GET,
            xhrFields: {
                withCredentials: true
            },
            contentType: "application/json",
            dataType: "json",
            success: function (response) {
                resolve(response.data);
            },
            error: function (xhr) {
                if(xhr.responseJSON.code === '1005') {
                    refreshTokenAjax();
                    return;
                }
                resolve(null);
            }
        });
    });
};

const adminCheckAjax = () => {
    return new Promise((resolve) => {
        $.ajax({
            url: apiPrefix + `/auth/check-admin`,
            type: AJAX_TYPE_GET,
            xhrFields: {
                withCredentials: true
            },
            contentType: "application/json",
            dataType: "json",
            success: function (response) {
                resolve(true);
            },
            error: function (xhr) {
                console.log(xhr);
                resolve(false);
            }
        });
    });
};
