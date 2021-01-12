var supportedDeviceListUrl = "https://spass-prd-cts-web.s3.amazonaws.com/supportedDeviceList.js"

var testMode = false;

var SPASS_WEB_SDK = {
    
    isSupported: function(callback) {
        
        //=====================================================================================
        // 1. check www platform.
        //=====================================================================================
        if (navigator.userAgent.match(/Android/i) && navigator.userAgent.match(/Chrome/i)) {
            // For mobile device, currently S-PASS support only Android.
            var verCheckPassed = false;
            var s = navigator.userAgent.match(/Android\s[0-9\.]*/i);
            if (s != null) {
                var ver = parseFloat(s[0].substring(8));
                if (ver >= 7.0) {
                    verCheckPassed = true;
                }
            }
            if (!verCheckPassed) {
                return false;
            }
        } else if (navigator.userAgent.match(/Windows/i) && navigator.userAgent.match(/.NET/i)) {
            // Just for test!
            testMode = true;
        } else {
            return false;
        }
        
        //=====================================================================================
        // 2. check device model.
        //=====================================================================================
        if (testMode) {
            callback(true); // Just say "found!".
        } else {
            $.getScript(supportedDeviceListUrl, function() {
                var found = false;
                for (var i = 0 ; i < supportedDeviceList.length ; i++) {
                    if (navigator.userAgent.indexOf(supportedDeviceList[i]) >= 0) {
                        found = true;
                        break;
                    }
                }
                
                found ? callback(true) : callback(false);
                return;
            });
        }
    },
    
    initiateSimpleLogin: function(val) {
            
        var deeplink = "svcBizCode=" + encodeURIComponent(val.svcBizCode) + 
            "&svcEventId=" + encodeURIComponent(val.svcEventId) + 
            "&siteId=" + encodeURIComponent(val.siteId) + 
            "&siteUrlHashEnc=" + encodeURIComponent(val.siteUrlHashEnc) + 
            "&seId=" + encodeURIComponent(val.seId) + 
            "&callbackUrl=" + encodeURIComponent(val.callbackUrl) + 
            "&siteTitle=" + encodeURIComponent(val.siteTitle) + 
            "&siteUrl=" + encodeURIComponent(val.siteUrl) + 
            "&siteIconUrl=" + encodeURIComponent(val.siteIconUrl) + 
            "&expireSec=" + encodeURIComponent(val.expireSec) + 
            "&senttime=" + encodeURIComponent(val.senttime) + 
            "&useCI=" + encodeURIComponent(val.useCI) + 
            (val.authnrType == null ? "" : "&authnrType=" + encodeURIComponent(val.authnrType));
            
        var intentUrl = [
                     "intent://deeplink?" + deeplink + "#Intent",
                     "scheme=samsungpass_simplelogin",
                     "package=com.samsung.android.samsungpass",
                     "S.browser_fallback_url=" + encodeURIComponent("http://www.samsungapps.com/appquery/appDetail.as?appId=com.samsung.android.samsungpass"),
                     "end"
                     ].join(";");
        
        if (testMode) {
            alert("\n\n******************************************\nTest mode activated! (Windows)\n******************************************\n\n\n@Deeplink result: --> " + intentUrl);
            return;
        }
        
        top.location.href = intentUrl;
    },
    
    initiateAuthService: function(val) {
        
        var deeplink = "opType=" + encodeURIComponent(val.opType) + 
            "&svcBizCode=" + encodeURIComponent(val.svcBizCode) + 
            "&svcEventId=" + encodeURIComponent(val.svcEventId) + 
            "&svcUserId=" + encodeURIComponent(val.svcUserId) + 
            "&siteId=" + encodeURIComponent(val.siteId) + 
            "&siteUrlHashEnc=" + encodeURIComponent(val.siteUrlHashEnc) + 
            "&seId=" + encodeURIComponent(val.seId) + 
            "&callbackUrl=" + encodeURIComponent(val.callbackUrl) + 
            "&siteTitle=" + encodeURIComponent(val.siteTitle) + 
            "&siteUrl=" + encodeURIComponent(val.siteUrl) + 
            "&siteIconUrl=" + encodeURIComponent(val.siteIconUrl) + 
            "&expireSec=" + encodeURIComponent(val.expireSec) + 
            "&senttime=" + encodeURIComponent(val.senttime) + 
            (val.authnrType == null ? "" : "&authnrType=" + encodeURIComponent(val.authnrType));
            
        var intentUrl = [
                     "intent://deeplink?" + deeplink + "#Intent",
                     "scheme=samsungpass_web_authentication",
                     "package=com.samsung.android.samsungpass",
                     "S.browser_fallback_url=" + encodeURIComponent("http://www.samsungapps.com/appquery/appDetail.as?appId=com.samsung.android.samsungpass"),
                     "end"
                     ].join(";");
                        
        if (testMode) {
            alert("\n\n******************************************\nTest mode activated! (Windows)\n******************************************\n\n\n@Deeplink result: --> " + intentUrl);
            return;
        }
        
        top.location.href = intentUrl;
    }
};
