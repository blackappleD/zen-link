$(function() {
    validateKickout();
    validateRule();
    refreshCode();
});

$.validator.setDefaults({
    submitHandler: function() {
        showVerfyImage();
    }
});

function showVerfyImage() {
	$("#verfyImg").find(".mask").css("display", "block");
}

function postLogin(data){
    $.modal.loading($("#btnSubmit").data("loading"));
    $.ajax({
        type: "post",
        url: ctx + "login",
        data: data,
        success: function(r) {
            if (r.code == web_status.SUCCESS) {
                location.href = ctx + 'index';
            } else {
            	$.modal.msg(r.msg);
            }
            $.modal.closeLoading();
        }
    });
}

/* 刷新验证码 */
function refreshCode() {
	/** 初始化验证码  弹出式 */
	var upCtType = captchaType?.toUpperCase();
	var defData = {};
	if(upCtType == 'DEFAULT') {
		upCtType = ['blockPuzzle', 'clickWord'][Math.floor(Math.random() * 2)];
		defData.captchaType = upCtType;
		upCtType = upCtType.toUpperCase();
	}
	var verify = upCtType == 'BLOCKPUZZLE' ? 'slideVerify' : 'pointsVerify';
	$('#verfyImg')[verify]({
		baseUrl: ctx,
		mode: 'pop',
		success : function(params) {
			var username = $.common.trim($("input[name='username']").val());
		    var password = $.common.trim($("input[name='password']").val());
		    var rememberMe = $("input[name='rememberme']").is(':checked');
		    var data = {
		        "username": username,
		        "password": password,
		        "rememberMe": rememberMe
		    };
			data = $.extend(data, params, defData);
			postLogin(data);
		},
		error : function() {}
	});
}

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            }
        },
        messages: {
            username: {
                required: icon + "请输入您的用户名",
            },
            password: {
                required: icon + "请输入您的密码",
            }
        }
    })
}

function validateKickout() {
    if (getParam("kickout") == 1) {
        layer.alert("<font color='red'>您已在别处登录，请您修改密码或重新登录</font>", {
            icon: 0,
            title: "系统提示"
        },
        function(index) {
            //关闭弹窗
            layer.close(index);
            if (top != self) {
                top.location = self.location;
            } else {
                var url = location.search;
                if (url) {
                    var oldUrl = window.location.href;
                    var newUrl = oldUrl.substring(0, oldUrl.indexOf('?'));
                    self.location = newUrl;
                }
            }
        });
    }
}

function getParam(paramName) {
    var reg = new RegExp("(^|&)" + paramName + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);
    return null;
}