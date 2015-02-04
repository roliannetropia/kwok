/*
 * ====================================================================
 * Copyright 2005-2011 Wai-Lun Kwok
 *
 * http://www.kwoksys.com/LICENSE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
var USER_STATUS_ENABLED = -1;
var URL_PARAM_AJAX = "_ajax";

function isEmptyString(string) {
    return string.replace(/^\s+|\s+$/g,"")=='';
}

function isEmptyElem(elemId) {
    var elem = document.getElementById(elemId);
    return isEmptyString(elem.innerHTML);
}

function getPosOffset(what, offsettype){
    var totaloffset=(offsettype=="left")? what.offsetLeft : what.offsetTop;
    var parentEl=what.offsetParent;
    while (parentEl != null) {
        totaloffset=(offsettype=="left")? totaloffset+parentEl.offsetLeft : totaloffset+parentEl.offsetTop;
        parentEl=parentEl.offsetParent;
    }
    return totaloffset;
}

function getSizes() {
    var width = 0, height = 0;
    if (typeof( window.innerWidth ) == 'number') {
        //Non-IE
        width = window.innerWidth;
        height = window.innerHeight;
    } else if (document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight )) {
        //IE 6+ in 'standards compliant mode'
        width = document.documentElement.clientWidth;
        height = document.documentElement.clientHeight;
    } else if (document.body && ( document.body.clientWidth || document.body.clientHeight )) {
        //IE 4 compatible
        width = document.body.clientWidth;
        height = document.body.clientHeight;
    }
    return Array(height, width);
}

function checkAll(thisObject, items) {
    for(i=0; i<items.length; i++){
        items[i].checked = thisObject.checked;
    }
}

function redirect(path) {
	document.location.href=path;
}

/*
 * This is for submiting a form when the user change selectbox value.
 * We'll be submitting to a different page according to formAction.
 */
function changeAction(thisObject, formAction) {
    thisObject.form.action = formAction;
    thisObject.form.submit();
}

/*
 * This is for submiting a form when the user change selectbox value.
 * We'll be submitting to what the form action is. -1 would stop the javascript from submitting.
 */
function changeSelectedOption(thisObject) {
    if (thisObject.value!=-1) {
        thisObject.form.submit();
    }
}

/*
 * This function is for disabling a button after the user clicks on the button.
 */
function disableButton(thisButton) {
    thisButton.disabled = true;
	thisButton.form.submit();
}

/*
 * Confirms form submit.
 */
function confirmSubmit(message) {
    if (confirm(message)) {
        return true;
    } else {
        return false;
    }
}

/*
 * This function is for disabling a button after the user clicks on the button.
 */
function disableButton2(thisButton, form) {
    thisButton.disabled = true;
	form.submit();
}

/*
 * This function is used to reset a form field value.
 */
function setFormFieldValue(formFieldName, formFieldValue) {
    formFieldName.value = formFieldValue;
}

/*
 * Get all options in a selectbox given a form field name.
 */
function selectAllOptions(formField) {
    for (i=0; i<formField.options.length; i++) {
        formField.options[i].selected = true;
    }
}

function moveOptions(fromList, toList) {
	for (i=0; i<fromList.options.length; i++) {
		var current = fromList.options[i];
		if (current.selected) {
			toList.options[toList.length] = new Option(current.text, current.value);
			fromList.options[i] = null;
			i--;
		}
	}
}

/*
 * Set style.display to empty.
 */
function showContent(elemId) {
	var elem = document.getElementById(elemId);
	if (elem) {
		elem.style.display="";
	}
}

/*
 * Set style.display to 'none'.
 */
function hideContent(elemId) {
	var elem = document.getElementById(elemId);
	if (elem) {
		elem.style.display="none";
	}
}

function showContents(elemArray) {
    for (i=0; i<elemArray.length; i++) {
        showContent(elemArray[i]);
    }
}

function hideContents(elemArray) {
    for (i=0; i<elemArray.length; i++) {
        hideContent(elemArray[i]);
    }
}

/*
 * Enables one elem and at the same time disable another elem.
 */
function swapViews(enableElemId, disableElemId) {
    hideContent(disableElemId);
    showContent(enableElemId);
}

function toggleView(elemId) {
    var isHidden = true;
	var elem = document.getElementById(elemId);
	if (elem) {
        if(elem.style.display == "none") {
            elem.style.display="block";
            isHidden = false;
		} else {
            elem.style.display="none";
		}
	}
    return isHidden;
}

function updateContent(elemId, content) {
    document.getElementById(elemId).innerHTML = content;
}

/*
 * Sets style.visibility to 'hidden'.
 */
function hideDivMenu(thisObject) {
	thisObject.style.visibility='hidden';
}

/*
 * Sets style.visibility to 'visible'.
 */
function showDivMenu(thisObject) {
	thisObject.style.visibility='visible';
}

/*
 * Sets style.visibility to 'hidden'.
 */
function hideElemById(elemId) {
	var elem = document.getElementById(elemId);
	if (elem) {
        hideDivMenu(elem);
    }
}

/*
 * Sets style.visibility to 'visible'.
 */
function showElemById(elemId) {
	var elem = document.getElementById(elemId);
	if (elem) {
        showDivMenu(elem);
    }
}

/*
 * Replaces replaceThis with replacedBy in input
 */
function replace(input, replaceThis, replacedBy) {
    var strLength = input.length;
	var txtLength = replaceThis.length;

    if ((strLength == 0) || (txtLength == 0)) {
		return input;
	}
    var i = input.indexOf(replaceThis);
    if ((!i) && (replaceThis != input.substring(0,txtLength))) {
		return input;
	}
    if (i == -1) {
		return input;
	}

    var newString = input.substring(0,i) + replacedBy;

    if (i+txtLength < strLength) {
        newString += replace(input.substring(i+txtLength,strLength),replaceThis,replacedBy);
	}
    return newString;
}

//
// Admin module
//
/*
 * This is for creating a new User. We want the display name field to reflect
 * what first name and last name are.
 */
function refreshDisplayName(formFirstName, formLastName, formDisplayName) {
	 formDisplayName.value = formFirstName.value + ' ' + formLastName.value;
}

/*
 * If user account status is enabled, password/confirm password fields are required, and vice versa. This script is
 * used to toggle the required field indicators "*".
 */
function tooglePasswordFields(status, formPassword, formConfirmPassword) {
    if (status == USER_STATUS_ENABLED) {
        showElemById(formPassword);
        showElemById(formConfirmPassword);
    } else {
        hideElemById(formPassword);
        hideElemById(formConfirmPassword);
    }
}

/**
 * Select all buttons for edit Permission
 * @param thisForm
 * @param list
 */
function selectAllAccessItems(elem, perms) {
    for (var i=0; i<perms.length; i++) {
        var formFields = elem.form.elements['formAccess_' + perms[i]];
        for(j=0; j<formFields.length; j++){
            if (formFields[j].value == elem.value) {
                formFields[j].checked = true;
                break;
            }
        }
    }
}

var ATTR_TYPE_STRING = 1;
var ATTR_TYPE_MULTILINE = 2;
var ATTR_TYPE_SELECTBOX = 3;
var ATTR_TYPE_RADIO_BUTTON = 4;
var ATTR_TYPE_DATE = 5;
var ATTR_TYPE_MULTISELECT = 6;
var ATTR_TYPE_CURRENCY = 7;

function attrOptions(attrTypeId) {
    showContents(['attrOptions', 'attrCurrencySymbol', 'convertUrl']);

    if (attrTypeId == ATTR_TYPE_STRING) {
        hideContents(['attrOptions', 'attrCurrencySymbol']);
    } else if (attrTypeId == ATTR_TYPE_MULTILINE) {
        hideContents(['attrOptions', 'attrCurrencySymbol']);
    } else if (attrTypeId == ATTR_TYPE_SELECTBOX) {
        hideContents(['convertUrl', 'attrCurrencySymbol']);
    } else if (attrTypeId == ATTR_TYPE_RADIO_BUTTON) {
        hideContents(['convertUrl', 'attrCurrencySymbol']);
    } else if (attrTypeId == ATTR_TYPE_DATE) {
        hideContents(['attrOptions', 'attrCurrencySymbol', 'convertUrl']);
    } else if (attrTypeId == ATTR_TYPE_CURRENCY) {
        hideContents(['attrOptions', 'convertUrl']);
    }
}

function checkLatestVersion(url) {
    invokeAjax(url, function callback(result) {
        if (result == document.getElementById('currentVersion').innerText) {
            // No upgrade needed
            showContent('noVersionUpgrade');
            hideContent('versionUpgrade');
        } else {
            // Need upgrade
            updateContent('latestVersion', result);
            hideContent('noVersionUpgrade');
            showContent('versionUpgrade');
        }
    });
}
//
// Auth module
//
function focusLogin(formUsername, formPassword) {
    // If the email field is empty, move the cursor there.
    // If the user has a saved email address, move the cursor to the password field.
	if (formUsername.value == '' ) {
		formUsername.focus();
	} else {
		formPassword.focus();
	}
}

//
// Blogs module
//
/*
 * Allows HTML preview when composing a new post.
 */
function refreshPostPreview(thisObject, id) {
	 updateContent(id, replace(replace(thisObject.value,'\r\n','<br>'),'\n','<br>'));
}

//
// Hardware module
//
function setWarrantyExpireYear(formName, year) {
    if (year == 0) {
        return;
    }

    document.forms[formName].warrantyMonth.value = document.forms[formName].purchaseMonth.value;
    document.forms[formName].warrantyDate.value = document.forms[formName].purchaseDate.value;

    var purchaseYear = document.forms[formName].purchaseYear.value;

    if (purchaseYear != 0) {
        purchaseYear = parseInt(purchaseYear) + parseInt(year);
    }
    document.forms[formName].warrantyYear.value = purchaseYear;
}

//
// Issues module
//
function toggleIssueDueDate(elem) {
    if (elem.value==1) {
        elem.form.dueDateMonth.disabled = false;
        elem.form.dueDateDate.disabled = false;
        elem.form.dueDateYear.disabled = false;
    } else {
        elem.form.dueDateMonth.disabled = true;
        elem.form.dueDateDate.disabled = true;
        elem.form.dueDateYear.disabled = true;
    }
}
function issueDisplayHtml() {
    hideContent("issueText");
    showContent("issueHtml");
    updateContent('issueHtmlContent', htmlspecialchars_decode(document.getElementById('issueTextContent').innerHTML));
}
function issueDisplayText() {
    showContent("issueText");
    hideContent("issueHtml");
}
//
// Portal module
//
/*
 * This would toggle an iFrame on/off.
 */
function toggleIframe(id, href){
	var thisIframe = document.getElementById(id);
	thisIframe.src = href;
}

//
// Reports module
//
function checkReportTypeEnabled(reportTypeField) {
    reportTypeField.form.sub.disabled = (reportTypeField.value == '');
}

//
// Ajax
//
function getHttpRequest() {
	var request;
    try {
        request = new ActiveXObject("Msxml2.XMLHTTP");
    } catch(e) {
        try {
            request = new ActiveXObject("Microsoft.XMLHTTP");
        } catch(e) {
            request = new XMLHttpRequest();
        }
    }
    return request;
}

function invokeAjax(url, callback) {
    var request = getHttpRequest();

 	request.open("GET", url, true);
	request.onreadystatechange = function() {
        // Ready State:
        // 0 - Uninitialised
        // 1 - Loading
        // 2 - Loaded
        // 3 - Interactive
        // 4 - Completed
        if (request.readyState == 4) {
            if (request.status == 200 || request.status == 401) {
                // Processing statements go here.
                response = request.responseXML.documentElement;
                result = response.getElementsByTagName('result')[0].firstChild.data;
				callback(result);
            }
        }
    };
	request.send(null);
}

function updateViewCallback(elemId, url, callback) {
    // Clears out old result
    updateContent(elemId, '<span class="image loadingImg">&nbsp;</span>');
    invokeAjax(url, callback);
}

function updateView(elemId, url) {
    updateViewCallback(elemId, url, function callback(result) {
        // Replace div area content with result.
        updateContent(elemId, result);

        // Executes javascript embedded in ajax.
        var scriptElems = document.getElementById(elemId).getElementsByTagName("script");
        for (i=0;i<scriptElems.length;i++) {
            eval(scriptElems[i].innerHTML);
//            var oScript = document.createElement('script');
//            oScript.text = scriptElems[i].innerHTML;
//            document.getElementsByTagName("head").item(0).appendChild(oScript);
        }
    });
}

function updateViewHistory(elemId, url) {
    if (history && history.pushState) {
        window.history.pushState(null, null, url);

        window.onpopstate = function(stackstate) {
            location.reload();
        };

        // The appending of ?/& and ajax=true must happen here instead of in java code. Or ajax=true will be
        // pushed to the browser history too.
        if (url.indexOf("?") == -1) {
            url += "?";
        } else {
            url += "&";
        }
        updateView(elemId, url + URL_PARAM_AJAX + '=true');
    } else {
        // For browsers not supporting pushState().
        goto(url);
    }
}

function updateViewLazy(elemId, url) {
    if (isEmptyElem(elemId)) {
        updateView(elemId, url);
    }
}

function toggleViewUpdateLazy(elemId, url) {
    var isHidden = toggleView(elemId);
    if (!isHidden) {
        updateViewLazy(elemId, url);
    }
}

function toggleViewUpdate(elemId, url) {
    var isHidden = toggleView(elemId);
    if (!isHidden) {
        updateView(elemId, url);
    }
}

/*
 * Gets hardware details in-page popup.
 */
function getHardwareDetail(div, url, thisObject, input, divPos) {
    invokeAjax(url + input, function callback(result) {
        var padding = 10;
        // Replace div area content with result.
        div.innerHTML = result;

        var offsetHeight = getPosOffset(thisObject, "top");
        var offsetLeft = getPosOffset(thisObject, "left") + 16;

        if (divPos == 'right') {
            offsetHeight -= (div.offsetHeight / 2);
        } else if (divPos == 'top') {
            offsetHeight -= div.offsetHeight + padding;
        }

        if (offsetHeight < 0) {
            offsetHeight = 0;
        }

        var sizes = getSizes();
        var height = sizes[0];
        var width = sizes[1];

        if ((offsetHeight + div.offsetHeight) > height) {
            offsetHeight -= (offsetHeight + div.offsetHeight) - height + padding;
        }

        if (width != 0 && (offsetLeft + div.offsetWidth) > width) {
            offsetLeft -= (offsetLeft + div.offsetWidth) - width + padding;
        }

        div.style.top = offsetHeight + 'px';
        div.style.left = offsetLeft + 'px';
    });
}

var cmenu = {
    divPos: 'right',
	popupDiv: '',
	url: '',
    input: '',

	dropit:function(thisObject, input){
		var thisDiv = document.getElementById(this.popupDiv);
        if (thisDiv) {
            if (thisDiv.style.visibility == 'visible' && (this.input == input)) {
                hideDivMenu(thisDiv);
            } else {
                // Clears out old result
                thisDiv.innerHTML = '';
                // Keeps track of last input
                this.input = input;
				// Shows div
                showDivMenu(thisDiv);
                getHardwareDetail(thisDiv, this.url, thisObject, input, this.divPos);
            }
        }
    }
};

function get_html_translation_table(table, quote_style) {
    // Returns the internal translation table used by htmlspecialchars and htmlentities  
    // 
    // version: 902.2516
    // discuss at: http://phpjs.org/functions/get_html_translation_table
    // +   original by: Philip Peterson
    // +    revised by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
    // +   bugfixed by: noname
    // +   bugfixed by: Alex
    // +   bugfixed by: Marco
    // %          note: It has been decided that we're not going to add global
    // %          note: dependencies to php.js. Meaning the constants are not
    // %          note: real constants, but strings instead. integers are also supported if someone
    // %          note: chooses to create the constants themselves.
    // %          note: Table from http://www.the-art-of-web.com/html/character-codes/
    // *     example 1: get_html_translation_table('HTML_SPECIALCHARS');
    // *     returns 1: {'"': '&quot;', '&': '&amp;', '<': '&lt;', '>': '&gt;'}
    
    var entities = {}, histogram = {}, decimal = 0, symbol = '';
    var constMappingTable = {}, constMappingQuoteStyle = {};
    var useTable = {}, useQuoteStyle = {};
    
    useTable      = (table ? table.toUpperCase() : 'HTML_SPECIALCHARS');
    useQuoteStyle = (quote_style ? quote_style.toUpperCase() : 'ENT_COMPAT');
    
    // Translate arguments
    constMappingTable[0]      = 'HTML_SPECIALCHARS';
    constMappingTable[1]      = 'HTML_ENTITIES';
    constMappingQuoteStyle[0] = 'ENT_NOQUOTES';
    constMappingQuoteStyle[2] = 'ENT_COMPAT';
    constMappingQuoteStyle[3] = 'ENT_QUOTES';
    
    // Map numbers to strings for compatibilty with PHP constants
    if (!isNaN(useTable)) {
        useTable = constMappingTable[useTable];
    }
    if (!isNaN(useQuoteStyle)) {
        useQuoteStyle = constMappingQuoteStyle[useQuoteStyle];
    }
    
    if (useQuoteStyle != 'ENT_NOQUOTES') {
        entities['34'] = '&quot;';
    }

    if (useQuoteStyle == 'ENT_QUOTES') {
        entities['39'] = '&#039;';
    }

    if (useTable == 'HTML_SPECIALCHARS') {
        // ascii decimals for better compatibility
        entities['38'] = '&amp;';
        entities['60'] = '&lt;';
        entities['62'] = '&gt;';
    } else if (useTable == 'HTML_ENTITIES') {
        // ascii decimals for better compatibility
	    entities['38']  = '&amp;';
	    entities['60']  = '&lt;';
	    entities['62']  = '&gt;';
	    entities['160'] = '&nbsp;';
	    entities['161'] = '&iexcl;';
	    entities['162'] = '&cent;';
	    entities['163'] = '&pound;';
	    entities['164'] = '&curren;';
	    entities['165'] = '&yen;';
	    entities['166'] = '&brvbar;';
	    entities['167'] = '&sect;';
	    entities['168'] = '&uml;';
	    entities['169'] = '&copy;';
	    entities['170'] = '&ordf;';
	    entities['171'] = '&laquo;';
	    entities['172'] = '&not;';
	    entities['173'] = '&shy;';
	    entities['174'] = '&reg;';
	    entities['175'] = '&macr;';
	    entities['176'] = '&deg;';
	    entities['177'] = '&plusmn;';
	    entities['178'] = '&sup2;';
	    entities['179'] = '&sup3;';
	    entities['180'] = '&acute;';
	    entities['181'] = '&micro;';
	    entities['182'] = '&para;';
	    entities['183'] = '&middot;';
	    entities['184'] = '&cedil;';
	    entities['185'] = '&sup1;';
	    entities['186'] = '&ordm;';
	    entities['187'] = '&raquo;';
	    entities['188'] = '&frac14;';
	    entities['189'] = '&frac12;';
	    entities['190'] = '&frac34;';
	    entities['191'] = '&iquest;';
	    entities['192'] = '&Agrave;';
	    entities['193'] = '&Aacute;';
	    entities['194'] = '&Acirc;';
	    entities['195'] = '&Atilde;';
	    entities['196'] = '&Auml;';
	    entities['197'] = '&Aring;';
	    entities['198'] = '&AElig;';
	    entities['199'] = '&Ccedil;';
	    entities['200'] = '&Egrave;';
	    entities['201'] = '&Eacute;';
	    entities['202'] = '&Ecirc;';
	    entities['203'] = '&Euml;';
	    entities['204'] = '&Igrave;';
	    entities['205'] = '&Iacute;';
	    entities['206'] = '&Icirc;';
	    entities['207'] = '&Iuml;';
	    entities['208'] = '&ETH;';
	    entities['209'] = '&Ntilde;';
	    entities['210'] = '&Ograve;';
	    entities['211'] = '&Oacute;';
	    entities['212'] = '&Ocirc;';
	    entities['213'] = '&Otilde;';
	    entities['214'] = '&Ouml;';
	    entities['215'] = '&times;';
	    entities['216'] = '&Oslash;';
	    entities['217'] = '&Ugrave;';
	    entities['218'] = '&Uacute;';
	    entities['219'] = '&Ucirc;';
	    entities['220'] = '&Uuml;';
	    entities['221'] = '&Yacute;';
	    entities['222'] = '&THORN;';
	    entities['223'] = '&szlig;';
	    entities['224'] = '&agrave;';
	    entities['225'] = '&aacute;';
	    entities['226'] = '&acirc;';
	    entities['227'] = '&atilde;';
	    entities['228'] = '&auml;';
	    entities['229'] = '&aring;';
	    entities['230'] = '&aelig;';
	    entities['231'] = '&ccedil;';
	    entities['232'] = '&egrave;';
	    entities['233'] = '&eacute;';
	    entities['234'] = '&ecirc;';
	    entities['235'] = '&euml;';
	    entities['236'] = '&igrave;';
	    entities['237'] = '&iacute;';
	    entities['238'] = '&icirc;';
	    entities['239'] = '&iuml;';
	    entities['240'] = '&eth;';
	    entities['241'] = '&ntilde;';
	    entities['242'] = '&ograve;';
	    entities['243'] = '&oacute;';
	    entities['244'] = '&ocirc;';
	    entities['245'] = '&otilde;';
	    entities['246'] = '&ouml;';
	    entities['247'] = '&divide;';
	    entities['248'] = '&oslash;';
	    entities['249'] = '&ugrave;';
	    entities['250'] = '&uacute;';
	    entities['251'] = '&ucirc;';
	    entities['252'] = '&uuml;';
	    entities['253'] = '&yacute;';
	    entities['254'] = '&thorn;';
	    entities['255'] = '&yuml;';
    } else {
        throw Error("Table: "+useTable+' not supported');
        return false;
    }
    
    // ascii decimals to real symbols
    for (decimal in entities) {
        symbol = String.fromCharCode(decimal);
        histogram[symbol] = entities[decimal];
    }
    
    return histogram;
}

function htmlspecialchars_decode(string, quote_style) {
    // Convert special HTML entities back to characters  
    // 
    // version: 901.714
    // discuss at: http://phpjs.org/functions/htmlspecialchars_decode
    // +   original by: Mirek Slugen
    // +   improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
    // +   bugfixed by: Mateusz "loonquawl" Zalega
    // +      input by: ReverseSyntax
    // +      input by: Slawomir Kaniecki
    // +      input by: Scott Cariss
    // +      input by: Francois
    // +   bugfixed by: Onno Marsman
    // +    revised by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
    // -    depends on: get_html_translation_table
    // *     example 1: htmlspecialchars_decode("<p>this -&gt; &quot;</p>", 'ENT_NOQUOTES');
    // *     returns 1: '<p>this -> &quot;</p>'
    var histogram = {}, symbol = '', tmp_str = '', entity = '';
    tmp_str = string.toString();
    
    if (false === (histogram = get_html_translation_table('HTML_SPECIALCHARS', quote_style))) {
        return false;
    }

    // &amp; must be the last character when decoding!
    delete(histogram['&']);
    histogram['&'] = '&amp;';

    for (symbol in histogram) {
        entity = histogram[symbol];
        tmp_str = tmp_str.split(entity).join(symbol);
    }
    
    return tmp_str;
}
