BasicDataHelper = function() {
    this.WebService = '/wcm/center.do';
    this.async = false;
    this.type = "post"
    this.err_msg_500 = "服务器异常：500"
}

BasicDataHelper.prototype = {
    Call: function(sServiceId, sMethodName, postData, successFn, errorFn, returnDataType, formatEncode) {
        var data = "";
        if (typeof formatEncode == "undefined") {
            formatEncode = "xml"
        }
        if (typeof returnDataType == "undefined") {
            returnDataType = "html"
        }
        if (typeof postData == "string") {
            data = this.buildStrXML(sServiceId, sMethodName, postData)
        } else if (typeof postData == "object") {
            data = this.buildJsonXML(sServiceId, sMethodName, postData)
        }
        $.ajax({
            type: this.type,
            async: this.async,
            data: data,
            url: this.WebService,
            dataType: returnDataType,
            success: function(xml) {
            	if(xml&&xml.indexOf&&xml.indexOf("<fault")>-1){
            		var Helper = new BasicDataHelper();
                    var jsonArr = $(Helper.createXMLDocument(xml));
                     errorFn(jsonArr.find("message").text(),xml);
            	}else if (formatEncode != 'xml' && returnDataType != "json" && typeof(xml) == "object") {
                    var Helper = new BasicDataHelper();
                    var jsonArr = Helper.XMLConversionToJSON(xml, true);
                    successFn(jsonArr)
                } else {
                	var root = (xml.nodeType == 9) ? xml.documentElement: xml;
                	var nn = root.localName || root.nodeName;
                	if(nn=="fault"){
                		var jsonArr = $(xml);
                        errorFn(jsonArr.find("message").text(),xml);
                	}else{
                		successFn(xml)
                	}
                }
            },
            error: function(){
            	errorFn(this.err_msg_500);
            }
        })
    },
    parseFormElements: function(postData) {
        var domChildrents = $('#' + postData).children();
        var valueStr = "<parameters>";
        for (var i = 0; i < domChildrents.length; i++) {
            var ChildrentName = domChildrents.eq(i).attr("name").toLowerCase();
            var ChildrentElement = domChildrents.eq(i).val();
            var hasCDATA = ChildrentElement.match(/<!\[CDATA\[.*\]\]>/mg);
            if (!hasCDATA) {
                ChildrentElement = "<![CDATA[" + ChildrentElement + "]]>"
            }
            valueStr += "<" + ChildrentName + ">" + ChildrentElement + "</" + ChildrentName + ">"
        }
        valueStr += "</parameters>";
        return valueStr
    },
    createXMLDocument:function(string) {
        var browserName = navigator.appName;
        var doc;
        if (browserName == 'Microsoft Internet Explorer') {
            doc = new ActiveXObject('Microsoft.XMLDOM');
            doc.async = 'false'
            doc.loadXML(string);
            } else {
            doc = (new DOMParser()).parseFromString(string, 'text/xml');
        }
        return doc;
    },
    parseJsonElements: function(json) {
        var str = "<parameters>";
        for (var jsonElement in json) {
            var jsonElementValue = json[jsonElement];
            jsonElement = jsonElement.toLowerCase();
            str += "<" + jsonElement + ">" + jsonElementValue + "</" + jsonElement + ">"
        }
        str += "</parameters>";
        return str
    },
    buildJsonXML: function(sServiceId, sMethodName, postData) {
        var myDoc = '<post-data><method type="' + sMethodName + '">' + sServiceId + '</method>' + this.parseJsonElements(postData) + '</post-data>';
        return myDoc
    },
    buildStrXML: function(sServiceId, sMethodName, postData) {
        var myDoc = '<post-data><method type="' + sMethodName + '">' + sServiceId + '</method>' + this.parseFormElements(postData) + '</post-data>';
        return myDoc
    },
    loadXML: function(xmlString) {
        var xmlDoc = null;
        if (!window.DOMParser && window.ActiveXObject) {
            var xmlDomVersions = ['MSXML.2.DOMDocument.6.0', 'MSXML.2.DOMDocument.3.0', 'Microsoft.XMLDOM'];
            for (var i = 0; i < xmlDomVersions.length; i++) {
                try {
                    xmlDoc = new ActiveXObject(xmlDomVersions[i]);
                    xmlDoc.async = false;
                    xmlDoc.loadXML(xmlString);
                    break
                } catch(e) {}
            }
        } else if (window.DOMParser && document.implementation && document.implementation.createDocument) {
            try {
                domParser = new DOMParser();
                xmlDoc = domParser.parseFromString(xmlString, 'text/xml')
            } catch(e) {}
        } else {
            return null
        }
        return xmlDoc
    },
    xml2str: function(xmldata) {
        if (window.ActiveXObject) {
            return xmldata.xml?xmldata.xml:(new XMLSerializer()).serializeToString(xmldata);
        } else {
            return (new XMLSerializer()).serializeToString(xmldata)
        }
    },
    removeRepeatJsonArr: function(jsonArr) {
        var currJsonArr = "";
        var currChildrenName = "";
        $.each(jsonArr,
        function(jsonArrKey, jsonArrValue) {
            currJsonArr = jsonArrValue
        });
        $.each(currJsonArr,
        function(key, value) {
            currChildrenName = key
        });
        currJsonArr[currChildrenName][0] = currJsonArr[currChildrenName][0];
        currJsonArr[currChildrenName].length = 1;
        return currJsonArr
    },
    XMLConversionToJSON: function(xml, extended) {
        var isRepeat = false;
        if (!xml) return {};
        function parseXML(node) {
            if (!node) return null;
            var txt = '',
            obj = null,
            att = null;
            var nt = node.nodeType,
            nn = jsVar(node.localName || node.nodeName);
            var nv = node.text || node.nodeValue || '';
            if (node.childNodes.length > 0) {
                $.each(node.childNodes,
                function(n, cn) {
                    var cnt = cn.nodeType,
                    cnn = jsVar(cn.localName || cn.nodeName);
                    var cnv = cn.text || cn.nodeValue || '';
                    if (cnt == 8) {
                        return
                    } else if (cnt == 3 || cnt == 4 || !cnn) {
                        if (cnv.match(/^\s+$/)) {
                            return
                        };
                        txt += cnv.replace(/^\s+/, '').replace(/\s+$/, '')
                    } else {
                        obj = obj || {};
                        if (obj[cnn]) {
                            if (!obj[cnn].length) obj[cnn] = myArr(obj[cnn]);
                            obj[cnn] = myArr(obj[cnn]);
                            obj[cnn][obj[cnn].length] = parseXML(cn);
                            obj[cnn].length = obj[cnn].length
                        } else {
                            obj[cnn] = parseXML(cn)
                        }
                    }
                })
            };
            if (obj) {
                obj = $.extend((txt != '' ? new String(txt) : {}), obj || {});
                txt = (obj.text) ? ([obj.text || '']).concat([txt]) : txt;
                if (txt) obj.text = txt;
                txt = ''
            };
            var out = obj || txt;
            if (extended) {
                txt = out.text || txt || ''
            };
            return out
        };
        var jsVar = function(s) {
            return String(s || '').replace(/-/g, "_")
        };
        function isNum(s) {
            var regexp = /^((-)?([0-9]+)(([\.\,]{0,1})([0-9]+))?$)/
            return (typeof s == "number") || regexp.test(String((s && typeof s == "string") ? jQuery.trim(s) : ''))
        };
        var myArr = function(o) {
            if (!$.isArray(o)) o = [o];
            o.length = o.length;
            return o
        };
        if (!xml.nodeType) return;
        if (xml.nodeType == 3 || xml.nodeType == 4) return xml.nodeValue;
        var root = (xml.nodeType == 9) ? xml.documentElement: xml;
        var maxLength = 0;
        var rnn =  jsVar(root.localName || root.nodeName);
        $.each($(xml).find(rnn).children(),
        function() {
            var crrElement = $(xml).find(rnn).children(this.tagName);
            var crrLength = crrElement.length;
            if (crrLength > maxLength) {
                maxLength = crrLength
            }
        });
        
        if (maxLength == 0 && $(xml).find(rnn).text() == "") {
            return null
        } else if (maxLength == 1) {
            isRepeat = true;
            var str = "<data>";
            str += this.xml2str(xml);
            str += this.xml2str(xml);
            str += "</data>";
            root = this.loadXML(str)
        }
        var out = parseXML(root);
        xml = null;
        root = null;
        if (isRepeat) {
            out = this.removeRepeatJsonArr(out)
        }
        return out
    }
};