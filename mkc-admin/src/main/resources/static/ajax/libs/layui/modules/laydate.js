/*! laydate 日期与时间组件 */
!function(window) {
    var MOD_NAME = "lay"
      , document = window.document
      , lay = function(selector) {
        return new LAY(selector)
    }
      , LAY = function(selector) {
        var index = 0
          , nativeDOM = typeof selector === "object" ? [selector] : (this.selector = selector,
        document.querySelectorAll(selector || null));
        for (; index < nativeDOM.length; index++) {
            this.push(nativeDOM[index])
        }
    };
    LAY.prototype = [];
    LAY.prototype.constructor = LAY;
    lay.extend = function() {
        var ai = 1, length, args = arguments, clone = function(target, obj) {
            target = target || (layui.type(obj) === "array" ? [] : {});
            for (var i in obj) {
                target[i] = (obj[i] && obj[i].constructor === Object) ? clone(target[i], obj[i]) : obj[i]
            }
            return target
        };
        args[0] = typeof args[0] === "object" ? args[0] : {};
        length = args.length;
        for (; ai < length; ai++) {
            if (typeof args[ai] === "object") {
                clone(args[0], args[ai])
            }
        }
        return args[0]
    }
    ;
    lay.v = "1.0.8";
    lay.ie = function() {
        var agent = navigator.userAgent.toLowerCase();
        return (!!window.ActiveXObject || "ActiveXObject"in window) ? ((agent.match(/msie\s(\d+)/) || [])[1] || "11") : false
    }();
    lay.layui = layui || {};
    lay.getPath = layui.cache.dir;
    lay.stope = layui.stope;
    lay.each = function() {
        layui.each.apply(layui, arguments);
        return this
    }
    ;
    lay.digit = function(num, length) {
        if (!(typeof num === "string" || typeof num === "number")) {
            return ""
        }
        var str = "";
        num = String(num);
        length = length || 2;
        for (var i = num.length; i < length; i++) {
            str += "0"
        }
        return num < Math.pow(10, length) ? str + num : num
    }
    ;
    lay.elem = function(elemName, attr) {
        var elem = document.createElement(elemName);
        lay.each(attr || {}, function(key, value) {
            elem.setAttribute(key, value)
        });
        return elem
    }
    ;
    lay.hasScrollbar = function() {
        return document.body.scrollHeight > (window.innerHeight || document.documentElement.clientHeight)
    }
    ;
    lay.position = function(elem, elemView, obj) {
        if (!elemView) {
            return
        }
        obj = obj || {};
        if (elem === document || elem === lay("body")[0]) {
            obj.clickType = "right"
        }
        var rect = obj.clickType === "right" ? function() {
            var e = obj.e || window.event || {};
            return {
                left: e.clientX,
                top: e.clientY,
                right: e.clientX,
                bottom: e.clientY
            }
        }() : elem.getBoundingClientRect()
          , elemWidth = elemView.offsetWidth
          , elemHeight = elemView.offsetHeight
          , scrollArea = function(type) {
            type = type ? "scrollLeft" : "scrollTop";
            return document.body[type] | document.documentElement[type]
        }
          , winArea = function(type) {
            return document.documentElement[type ? "clientWidth" : "clientHeight"]
        }
          , margin = 5
          , left = rect.left
          , top = rect.bottom;
        if (obj.align === "center") {
            left = left - (elemWidth - elem.offsetWidth) / 2
        } else {
            if (obj.align === "right") {
                left = left - elemWidth + elem.offsetWidth
            }
        }
        if (left + elemWidth + margin > winArea("width")) {
            left = winArea("width") - elemWidth - margin
        }
        if (left < margin) {
            left = margin
        }
        if (top + elemHeight + margin > winArea()) {
            if (rect.top > elemHeight + margin) {
                top = rect.top - elemHeight - margin * 2
            } else {
                if (obj.clickType === "right") {
                    top = winArea() - elemHeight - margin * 2;
                    if (top < 0) {
                        top = 0
                    }
                } else {
                    top = margin
                }
            }
        }
        var position = obj.position;
        if (position) {
            elemView.style.position = position
        }
        elemView.style.left = left + (position === "fixed" ? 0 : scrollArea(1)) + "px";
        elemView.style.top = top + (position === "fixed" ? 0 : scrollArea()) + "px";
        if (!lay.hasScrollbar()) {
            var rect1 = elemView.getBoundingClientRect();
            if (!obj.SYSTEM_RELOAD && (rect1.bottom + margin) > winArea()) {
                obj.SYSTEM_RELOAD = true;
                setTimeout(function() {
                    lay.position(elem, elemView, obj)
                }, 50)
            }
        }
    }
    ;
    lay.options = function(elem, attr) {
        var othis = lay(elem)
          , attrName = attr || "lay-options";
        try {
            return new Function("return " + (othis.attr(attrName) || "{}"))()
        } catch (ev) {
            hint.error("parseerror：" + ev, "error");
            return {}
        }
    }
    ;
    lay.isTopElem = function(elem) {
        var topElems = [document, lay("body")[0]]
          , matched = false;
        lay.each(topElems, function(index, item) {
            if (item === elem) {
                return matched = true
            }
        });
        return matched
    }
    ;
    LAY.addStr = function(str, new_str) {
        str = str.replace(/\s+/, " ");
        new_str = new_str.replace(/\s+/, " ").split(" ");
        lay.each(new_str, function(ii, item) {
            if (!new RegExp("\\b" + item + "\\b").test(str)) {
                str = str + " " + item
            }
        });
        return str.replace(/^\s|\s$/, "")
    }
    ;
    LAY.removeStr = function(str, new_str) {
        str = str.replace(/\s+/, " ");
        new_str = new_str.replace(/\s+/, " ").split(" ");
        lay.each(new_str, function(ii, item) {
            var exp = new RegExp("\\b" + item + "\\b");
            if (exp.test(str)) {
                str = str.replace(exp, "")
            }
        });
        return str.replace(/\s+/, " ").replace(/^\s|\s$/, "")
    }
    ;
    LAY.prototype.find = function(selector) {
        var that = this;
        var index = 0
          , arr = []
          , isObject = typeof selector === "object";
        this.each(function(i, item) {
            var nativeDOM = isObject ? item.contains(selector) : item.querySelectorAll(selector || null);
            for (; index < nativeDOM.length; index++) {
                arr.push(nativeDOM[index])
            }
            that.shift()
        });
        if (!isObject) {
            that.selector = (that.selector ? that.selector + " " : "") + selector
        }
        lay.each(arr, function(i, item) {
            that.push(item)
        });
        return that
    }
    ;
    LAY.prototype.each = function(fn) {
        return lay.each.call(this, this, fn)
    }
    ;
    LAY.prototype.addClass = function(className, type) {
        return this.each(function(index, item) {
            item.className = LAY[type ? "removeStr" : "addStr"](item.className, className)
        })
    }
    ;
    LAY.prototype.removeClass = function(className) {
        return this.addClass(className, true)
    }
    ;
    LAY.prototype.hasClass = function(className) {
        var has = false;
        this.each(function(index, item) {
            if (new RegExp("\\b" + className + "\\b").test(item.className)) {
                has = true
            }
        });
        return has
    }
    ;
    LAY.prototype.css = function(key, value) {
        var that = this
          , parseValue = function(v) {
            return isNaN(v) ? v : (v + "px")
        };
        return (typeof key === "string" && value === undefined) ? function() {
            if (that.length > 0) {
                return that[0].style[key]
            }
        }() : that.each(function(index, item) {
            typeof key === "object" ? lay.each(key, function(thisKey, thisValue) {
                item.style[thisKey] = parseValue(thisValue)
            }) : item.style[key] = parseValue(value)
        })
    }
    ;
    LAY.prototype.width = function(value) {
        var that = this;
        return value === undefined ? function() {
            if (that.length > 0) {
                return that[0].offsetWidth
            }
        }() : that.each(function(index, item) {
            that.css("width", value)
        })
    }
    ;
    LAY.prototype.height = function(value) {
        var that = this;
        return value === undefined ? function() {
            if (that.length > 0) {
                return that[0].offsetHeight
            }
        }() : that.each(function(index, item) {
            that.css("height", value)
        })
    }
    ;
    LAY.prototype.attr = function(key, value) {
        var that = this;
        return value === undefined ? function() {
            if (that.length > 0) {
                return that[0].getAttribute(key)
            }
        }() : that.each(function(index, item) {
            item.setAttribute(key, value)
        })
    }
    ;
    LAY.prototype.removeAttr = function(key) {
        return this.each(function(index, item) {
            item.removeAttribute(key)
        })
    }
    ;
    LAY.prototype.html = function(html) {
        var that = this;
        return html === undefined ? function() {
            if (that.length > 0) {
                return that[0].innerHTML
            }
        }() : this.each(function(index, item) {
            item.innerHTML = html
        })
    }
    ;
    LAY.prototype.val = function(value) {
        var that = this;
        return value === undefined ? function() {
            if (that.length > 0) {
                return that[0].value
            }
        }() : this.each(function(index, item) {
            item.value = value
        })
    }
    ;
    LAY.prototype.append = function(elem) {
        return this.each(function(index, item) {
            typeof elem === "object" ? item.appendChild(elem) : item.innerHTML = item.innerHTML + elem
        })
    }
    ;
    LAY.prototype.remove = function(elem) {
        return this.each(function(index, item) {
            elem ? item.removeChild(elem) : item.parentNode.removeChild(item)
        })
    }
    ;
    LAY.prototype.on = function(eventName, fn) {
        return this.each(function(index, item) {
            item.attachEvent ? item.attachEvent("on" + eventName, function(e) {
                e.target = e.srcElement;
                fn.call(item, e)
            }) : item.addEventListener(eventName, fn, false)
        })
    }
    ;
    LAY.prototype.off = function(eventName, fn) {
        return this.each(function(index, item) {
            item.detachEvent ? item.detachEvent("on" + eventName, fn) : item.removeEventListener(eventName, fn, false)
        })
    }
    ;
    window.lay = lay;
    if (window.layui && layui.define) {
        layui.define(function(exports) {
            exports(MOD_NAME, lay)
        })
    }
}(window, window.document);
!function(window, document) {
    var isLayui = window.layui && layui.define
      , ready = {
        getPath: (window.lay && lay.getPath) ? lay.getPath : "",
        link: function(href, fn, cssname) {
            if (!laydate.path) {
                return
            }
            if (window.lay && lay.layui) {
                lay.layui.link(laydate.path + href, fn, cssname)
            }
        }
    }
      , GLOBAL = window.LAYUI_GLOBAL || {}
      , laydate = {
        v: "5.3.1",
        config: {
            weekStart: 0,
        },
        index: (window.laydate && window.laydate.v) ? 100000 : 0,
        path: GLOBAL.laydate_dir || ready.getPath,
        set: function(options) {
            var that = this;
            that.config = lay.extend({}, that.config, options);
            return that
        },
        ready: function(fn) {
            var cssname = "laydate"
              , ver = ""
              , path = (isLayui ? "modules/laydate/" : "theme/") + "default/laydate.css?v=" + laydate.v + ver;
            isLayui ? layui.addcss(path, fn, cssname) : ready.link(path, fn, cssname);
            return this
        }
    }
      , thisModule = function() {
        var that = this
          , options = that.config
          , id = options.id;
        thisModule.that[id] = that;
        return {
            hint: function(content) {
                that.hint.call(that, content)
            },
            config: that.config
        }
    }
      , MOD_NAME = "laydate"
      , ELEM = ".layui-laydate"
      , THIS = "layui-this"
      , SHOW = "layui-show"
      , HIDE = "layui-hide"
      , DISABLED = "laydate-disabled"
      , LIMIT_YEAR = [100, 200000]
      , ELEM_STATIC = "layui-laydate-static"
      , ELEM_LIST = "layui-laydate-list"
      , ELEM_SELECTED = "laydate-selected"
      , ELEM_HINT = "layui-laydate-hint"
      , ELEM_PREV = "laydate-day-prev"
      , ELEM_NEXT = "laydate-day-next"
      , ELEM_FOOTER = "layui-laydate-footer"
      , ELEM_CONFIRM = ".laydate-btns-confirm"
      , ELEM_TIME_TEXT = "laydate-time-text"
      , ELEM_TIME_BTN = "laydate-btns-time"
      , ELEM_PREVIEW = "layui-laydate-preview"
      , Class = function(options) {
        var that = this;
        that.index = ++laydate.index;
        that.config = lay.extend({}, that.config, laydate.config, options);
        var elem = lay(options.elem || that.config.elem);
        if (elem.length > 1) {
            layui.each(elem, function() {
                laydate.render(lay.extend({}, that.config, {
                    elem: this
                }))
            });
            return that
        }
        options = that.config;
        options.id = ("id"in options) ? options.id : that.index;
        laydate.ready(function() {
            that.init()
        })
    }
      , dateType = "yyyy|y|MM|M|dd|d|HH|H|mm|m|ss|s";
    thisModule.formatArr = function(format) {
        return (format || "").match(new RegExp(dateType + "|.","g")) || []
    }
    ;
    Class.isLeapYear = function(year) {
        return (year % 4 === 0 && year % 100 !== 0) || year % 400 === 0
    }
    ;
    Class.prototype.config = {
        type: "date",
        range: false,
        format: "yyyy-MM-dd",
        value: null,
        isInitValue: true,
        min: "1900-1-1",
        max: "2099-12-31",
        trigger: "click",
        show: false,
        showBottom: true,
        isPreview: true,
        btns: ["clear", "now", "confirm"],
        lang: "cn",
        theme: "default",
        position: null,
        calendar: false,
        mark: {},
        holidays: null,
        zIndex: null,
        done: null,
        change: null
    };
    Class.prototype.lang = function() {
        var that = this
          , options = that.config
          , text = {
            cn: {
                weeks: ["日", "一", "二", "三", "四", "五", "六"],
                time: ["时", "分", "秒"],
                timeTips: "选择时间",
                startTime: "开始时间",
                endTime: "结束时间",
                dateTips: "返回日期",
                month: ["一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"],
                tools: {
                    confirm: "确定",
                    clear: "清空",
                    now: "现在"
                },
                timeout: "结束时间不能早于开始时间<br>请重新选择",
                invalidDate: "不在有效日期或时间范围内",
                formatError: ["日期格式不合法<br>必须遵循下述格式：<br>", "<br>已为你重置"],
                preview: "当前选中的结果"
            },
            en: {
                weeks: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"],
                time: ["Hours", "Minutes", "Seconds"],
                timeTips: "Select Time",
                startTime: "Start Time",
                endTime: "End Time",
                dateTips: "Select Date",
                month: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
                tools: {
                    confirm: "Confirm",
                    clear: "Clear",
                    now: "Now"
                },
                timeout: "End time cannot be less than start Time<br>Please re-select",
                invalidDate: "Invalid date",
                formatError: ["The date format error<br>Must be followed：<br>", "<br>It has been reset"],
                preview: "The selected result"
            }
        };
        return text[options.lang] || text["cn"]
    }
    ;
    Class.prototype.init = function() {
        var that = this
          , options = that.config
          , isStatic = options.position === "static"
          , format = {
            year: "yyyy",
            month: "yyyy-MM",
            date: "yyyy-MM-dd",
            time: "HH:mm:ss",
            datetime: "yyyy-MM-dd HH:mm:ss"
        };
        options.elem = lay(options.elem);
        options.eventElem = lay(options.eventElem);
        if (!options.elem[0]) {
            return
        }
        that.rangeStr = options.range ? (typeof options.range === "string" ? options.range : "-") : "";
        if (layui.type(options.range) === "array") {
            that.rangeElem = [lay(options.range[0]), lay(options.range[1])]
        }
        if (!format[options.type]) {
            window.console && console.error && console.error("laydate type error:'" + options.type + "' is not supported");
            options.type = "date"
        }
        if (options.format === format.date) {
            options.format = format[options.type] || format.date
        }
        that.format = thisModule.formatArr(options.format);
        if (options.weekStart) {
            if (!/^[0-6]$/.test(options.weekStart)) {
                var lang = that.lang();
                options.weekStart = lang.weeks.indexOf(options.weekStart);
                if (options.weekStart === -1) {
                    options.weekStart = 0
                }
            }
        }
        that.EXP_IF = "";
        that.EXP_SPLIT = "";
        lay.each(that.format, function(i, item) {
            var EXP = new RegExp(dateType).test(item) ? "\\d{" + function() {
                if (new RegExp(dateType).test(that.format[i === 0 ? i + 1 : i - 1] || "")) {
                    if (/^yyyy|y$/.test(item)) {
                        return 4
                    }
                    return item.length
                }
                if (/^yyyy$/.test(item)) {
                    return "1,4"
                }
                if (/^y$/.test(item)) {
                    return "1,308"
                }
                return "1,2"
            }() + "}" : "\\" + item;
            that.EXP_IF = that.EXP_IF + EXP;
            that.EXP_SPLIT = that.EXP_SPLIT + "(" + EXP + ")"
        });
        that.EXP_IF_ONE = new RegExp("^" + that.EXP_IF + "$");
        that.EXP_IF = new RegExp("^" + (options.range ? that.EXP_IF + "\\s\\" + that.rangeStr + "\\s" + that.EXP_IF : that.EXP_IF) + "$");
        that.EXP_SPLIT = new RegExp("^" + that.EXP_SPLIT + "$","");
        if (!that.isInput(options.elem[0])) {
            if (options.trigger === "focus") {
                options.trigger = "click"
            }
        }
        if (!options.elem.attr("lay-key")) {
            options.elem.attr("lay-key", that.index);
            options.eventElem.attr("lay-key", that.index)
        }
        options.mark = lay.extend({}, (options.calendar && options.lang === "cn") ? {
            "0-1-1": "元旦",
            "0-2-14": "情人",
            "0-3-8": "妇女",
            "0-3-12": "植树",
            "0-4-1": "愚人",
            "0-5-1": "劳动",
            "0-5-4": "青年",
            "0-6-1": "儿童",
            "0-9-10": "教师",
            "0-10-1": "国庆",
            "0-12-25": "圣诞"
        } : {}, options.mark);
        lay.each(["min", "max"], function(i, item) {
            var ymd = [], hms = [], dfymd = Class.prototype.config[item].split("-");
            if (typeof options[item] === "number") {
                var day = options[item]
                  , tDate = new Date()
                  , time = that.newDate({
                    year: tDate.getFullYear(),
                    month: tDate.getMonth(),
                    date: tDate.getDate(),
                    hours: i ? 23 : 0,
                    minutes: i ? 59 : 0,
                    seconds: i ? 59 : 0
                }).getTime()
                  , STAMP = 86400000
                  , thisDate = new Date(day ? (day < STAMP ? time + day * STAMP : day) : time);
                ymd = [thisDate.getFullYear(), thisDate.getMonth() + 1, thisDate.getDate()];
                hms = [thisDate.getHours(), thisDate.getMinutes(), thisDate.getSeconds()]
            } else {
                ymd = (options[item] || '').split("-");
                hms = (options[item] || '').split(":")
            }
            options[item] = {
                year: ymd[0] | 0 || dfymd[0] | 0,
                month: (ymd[1] | 0 || dfymd[1] | 0) - 1,
                date: ymd[2] | 0 || dfymd[2] | 0,
                hours: hms[0] | 0,
                minutes: hms[1] | 0,
                seconds: hms[2] | 0
            }
        });
        that.elemID = "layui-laydate" + options.elem.attr("lay-key");
        if (options.show || isStatic) {
            that.render()
        }
        isStatic || that.events();
        if (options.value && options.isInitValue) {
            if (layui.type(options.value) === "date") {
                that.setValue(that.parse(0, that.systemDate(options.value)))
            } else {
                that.setValue(options.value)
            }
        }
    }
    ;
    Class.prototype.render = function() {
        var that = this
          , options = that.config
          , lang = that.lang()
          , isStatic = options.position === "static"
          , elem = that.elem = lay.elem("div", {
            id: that.elemID,
            "class": ["layui-laydate", options.range ? " layui-laydate-range" : "", isStatic ? (" " + ELEM_STATIC) : "", options.theme && options.theme !== "default" && !/^#/.test(options.theme) ? (" laydate-theme-" + options.theme) : ""].join("")
        })
          , elemMain = that.elemMain = []
          , elemHeader = that.elemHeader = []
          , elemCont = that.elemCont = []
          , elemTable = that.table = []
          , divFooter = that.footer = lay.elem("div", {
            "class": ELEM_FOOTER
        });
        if (options.zIndex) {
            elem.style.zIndex = options.zIndex
        }
        lay.each(new Array(2), function(i) {
            if (!options.range && i > 0) {
                return true
            }
            var divHeader = lay.elem("div", {
                "class": "layui-laydate-header"
            })
              , headerChild = [function() {
                var elem = lay.elem("i", {
                    "class": "layui-icon laydate-icon laydate-prev-y"
                });
                elem.innerHTML = "&#xe65a;";
                return elem
            }(), function() {
                var elem = lay.elem("i", {
                    "class": "layui-icon laydate-icon laydate-prev-m"
                });
                elem.innerHTML = "&#xe603;";
                return elem
            }(), function() {
                var elem = lay.elem("div", {
                    "class": "laydate-set-ym"
                })
                  , spanY = lay.elem("span")
                  , spanM = lay.elem("span");
                elem.appendChild(spanY);
                elem.appendChild(spanM);
                return elem
            }(), function() {
                var elem = lay.elem("i", {
                    "class": "layui-icon laydate-icon laydate-next-m"
                });
                elem.innerHTML = "&#xe602;";
                return elem
            }(), function() {
                var elem = lay.elem("i", {
                    "class": "layui-icon laydate-icon laydate-next-y"
                });
                elem.innerHTML = "&#xe65b;";
                return elem
            }()]
              , divContent = lay.elem("div", {
                "class": "layui-laydate-content"
            })
              , table = lay.elem("table")
              , thead = lay.elem("thead")
              , theadTr = lay.elem("tr");
            lay.each(headerChild, function(i, item) {
                divHeader.appendChild(item)
            });
            thead.appendChild(theadTr);
            lay.each(new Array(6), function(i) {
                var tr = table.insertRow(0);
                lay.each(new Array(7), function(j) {
                    if (i === 0) {
                        var th = lay.elem("th");
                        th.innerHTML = lang.weeks[(j + options.weekStart) % 7];
                        theadTr.appendChild(th)
                    }
                    tr.insertCell(j)
                })
            });
            table.insertBefore(thead, table.children[0]);
            divContent.appendChild(table);
            elemMain[i] = lay.elem("div", {
                "class": "layui-laydate-main laydate-main-list-" + i
            });
            elemMain[i].appendChild(divHeader);
            elemMain[i].appendChild(divContent);
            elemHeader.push(headerChild);
            elemCont.push(divContent);
            elemTable.push(table)
        });
        lay(divFooter).html(function() {
            var html = []
              , btns = [];
            if (options.type === "datetime") {
                html.push('<span lay-type="datetime" class="' + ELEM_TIME_BTN + '">' + lang.timeTips + "</span>")
            }
            if (!(!options.range && options.type === "datetime")) {
                html.push('<span class="' + ELEM_PREVIEW + '" title="' + lang.preview + '"></span>')
            }
            lay.each(options.btns, function(i, item) {
                var title = lang.tools[item] || "btn";
                if (options.range && item === "now") {
                    return
                }
                if (isStatic && item === "clear") {
                    title = options.lang === "cn" ? "重置" : "Reset"
                }
                btns.push('<span lay-type="' + item + '" class="laydate-btns-' + item + '">' + title + "</span>")
            });
            html.push('<div class="laydate-footer-btns">' + btns.join("") + "</div>");
            return html.join("")
        }());
        lay.each(elemMain, function(i, main) {
            elem.appendChild(main)
        });
        options.showBottom && elem.appendChild(divFooter);
        if (/^#/.test(options.theme)) {
            var style = lay.elem("style")
              , styleText = ["#{{id}} .layui-laydate-header{background-color:{{theme}};}", "#{{id}} .layui-this{background-color:{{theme}} !important;}"].join("").replace(/{{id}}/g, that.elemID).replace(/{{theme}}/g, options.theme);
            if ("styleSheet"in style) {
                style.setAttribute("type", "text/css");
                style.styleSheet.cssText = styleText
            } else {
                style.innerHTML = styleText
            }
            lay(elem).addClass("laydate-theme-molv");
            elem.appendChild(style)
        }
        that.remove(Class.thisElemDate);
        laydate.thisId = options.id;
        isStatic ? options.elem.append(elem) : (document.body.appendChild(elem),
        that.position());
        that.checkDate().calendar(null, 0, "init");
        that.changeEvent();
        Class.thisElemDate = that.elemID;
        typeof options.ready === "function" && options.ready(lay.extend({}, options.dateTime, {
            month: options.dateTime.month + 1
        }));
        that.preview()
    }
    ;
    Class.prototype.remove = function(prev) {
        var that = this
          , options = that.config
          , elem = lay("#" + (prev || that.elemID));
        if (!elem[0]) {
            return that
        }
        if (!elem.hasClass(ELEM_STATIC)) {
            that.checkDate(function() {
                elem.remove();
                delete laydate.thisId;
                typeof options.close === "function" && options.close(that)
            })
        }
        return that
    }
    ;
    Class.prototype.position = function() {
        var that = this
          , options = that.config;
        lay.position(that.bindElem || options.elem[0], that.elem, {
            position: options.position
        });
        return that
    }
    ;
    Class.prototype.hint = function(content) {
        var that = this
          , options = that.config
          , div = lay.elem("div", {
            "class": ELEM_HINT
        });
        if (!that.elem) {
            return
        }
        div.innerHTML = content || "";
        lay(that.elem).find("." + ELEM_HINT).remove();
        that.elem.appendChild(div);
        clearTimeout(that.hinTimer);
        that.hinTimer = setTimeout(function() {
            lay(that.elem).find("." + ELEM_HINT).remove()
        }, 3000)
    }
    ;
    Class.prototype.getAsYM = function(Y, M, type) {
        type ? M-- : M++;
        if (M < 0) {
            M = 11;
            Y--
        }
        if (M > 11) {
            M = 0;
            Y++
        }
        return [Y, M]
    }
    ;
    Class.prototype.systemDate = function(newDate) {
        var thisDate = newDate || new Date();
        return {
            year: thisDate.getFullYear(),
            month: thisDate.getMonth(),
            date: thisDate.getDate(),
            hours: newDate ? newDate.getHours() : 0,
            minutes: newDate ? newDate.getMinutes() : 0,
            seconds: newDate ? newDate.getSeconds() : 0
        }
    }
    ;
    Class.prototype.checkDate = function(fn) {
        var that = this, thisDate = new Date(), options = that.config, lang = that.lang(), dateTime = options.dateTime = options.dateTime || that.systemDate(), thisMaxDate, error, elem = that.bindElem || options.elem[0], valType = that.isInput(elem) ? "val" : "html", value = function() {
            if (that.rangeElem) {
                var vals = [that.rangeElem[0].val(), that.rangeElem[1].val()];
                if (vals[0] && vals[1]) {
                    return vals.join(" " + that.rangeStr + " ")
                }
            }
            return that.isInput(elem) ? elem.value : (options.position === "static" ? "" : lay(elem).attr("lay-date"))
        }(), checkValid = function(dateTime) {
            if (dateTime.year > LIMIT_YEAR[1]) {
                dateTime.year = LIMIT_YEAR[1],
                error = true
            }
            if (dateTime.month > 11) {
                dateTime.month = 11,
                error = true
            }
            if (dateTime.seconds > 59) {
                dateTime.seconds = 0,
                dateTime.minutes++,
                error = true
            }
            if (dateTime.minutes > 59) {
                dateTime.minutes = 0,
                dateTime.hours++,
                error = true
            }
            if (dateTime.hours > 23) {
                dateTime.hours = 0,
                error = true
            }
            thisMaxDate = laydate.getEndDate(dateTime.month + 1, dateTime.year);
            if (dateTime.date > thisMaxDate) {
                dateTime.date = thisMaxDate,
                error = true
            }
        }, initDate = function(dateTime, value, index) {
            var startEnd = ["startTime", "endTime"];
            value = (value.match(that.EXP_SPLIT) || []).slice(1);
            index = index || 0;
            if (options.range) {
                that[startEnd[index]] = that[startEnd[index]] || {}
            }
            lay.each(that.format, function(i, item) {
                var thisv = parseFloat(value[i]);
                if (value[i].length < item.length) {
                    error = true
                }
                if (/yyyy|y/.test(item)) {
                    if (thisv < LIMIT_YEAR[0]) {
                        thisv = LIMIT_YEAR[0],
                        error = true
                    }
                    dateTime.year = thisv
                } else {
                    if (/MM|M/.test(item)) {
                        if (thisv < 1) {
                            thisv = 1,
                            error = true
                        }
                        dateTime.month = thisv - 1
                    } else {
                        if (/dd|d/.test(item)) {
                            if (thisv < 1) {
                                thisv = 1,
                                error = true
                            }
                            dateTime.date = thisv
                        } else {
                            if (/HH|H/.test(item)) {
                                if (thisv < 0) {
                                    thisv = 0,
                                    error = true
                                }
                                if (thisv > 23) {
                                    thisv = 23,
                                    error = true
                                }
                                dateTime.hours = thisv;
                                options.range && (that[startEnd[index]].hours = thisv)
                            } else {
                                if (/mm|m/.test(item)) {
                                    if (thisv < 0) {
                                        thisv = 0,
                                        error = true
                                    }
                                    if (thisv > 59) {
                                        thisv = 59,
                                        error = true
                                    }
                                    dateTime.minutes = thisv;
                                    options.range && (that[startEnd[index]].minutes = thisv)
                                } else {
                                    if (/ss|s/.test(item)) {
                                        if (thisv < 0) {
                                            thisv = 0,
                                            error = true
                                        }
                                        if (thisv > 59) {
                                            thisv = 59,
                                            error = true
                                        }
                                        dateTime.seconds = thisv;
                                        options.range && (that[startEnd[index]].seconds = thisv)
                                    }
                                }
                            }
                        }
                    }
                }
            });
            checkValid(dateTime)
        };
        if (fn === "limit") {
            return checkValid(dateTime),
            that
        }
        value = value || options.value;
        if (typeof value === "string") {
            value = value.replace(/\s+/g, " ").replace(/^\s|\s$/g, "")
        }
        var getEndDate = function() {
            if (options.range) {
                that.endDate = that.endDate || lay.extend({}, options.dateTime, function() {
                    var obj = {}
                      , dateTime = options.dateTime
                      , EYM = that.getAsYM(dateTime.year, dateTime.month);
                    if (options.type === "year") {
                        obj.year = dateTime.year + 1
                    } else {
                        if (options.type !== "time") {
                            obj.year = EYM[0];
                            obj.month = EYM[1]
                        }
                    }
                    if (options.type === "datetime" || options.type === "time") {
                        obj.hours = 23;
                        obj.minutes = obj.seconds = 59
                    }
                    return obj
                }())
            }
        };
        getEndDate();
        if (typeof value === "string" && value) {
            if (that.EXP_IF.test(value)) {
                if (options.range) {
                    value = value.split(" " + that.rangeStr + " ");
                    lay.each([options.dateTime, that.endDate], function(i, item) {
                        initDate(item, value[i], i)
                    })
                } else {
                    initDate(dateTime, value)
                }
            } else {
                that.hint(lang.formatError[0] + (options.range ? (options.format + " " + that.rangeStr + " " + options.format) : options.format) + lang.formatError[1]);
                error = true
            }
        } else {
            if (value && layui.type(value) === "date") {
                options.dateTime = that.systemDate(value)
            } else {
                options.dateTime = that.systemDate();
                delete that.startTime;
                delete that.endDate;
                getEndDate();
                delete that.endTime
            }
        }
        (function() {
            if (that.rangeElem) {
                var vals = [that.rangeElem[0].val(), that.rangeElem[1].val()]
                  , arrDate = [options.dateTime, that.endDate];
                lay.each(vals, function(_i, _v) {
                    if (that.EXP_IF_ONE.test(_v)) {
                        initDate(arrDate[_i], _v, _i)
                    }
                })
            }
        }
        )();
        checkValid(dateTime);
        if (options.range) {
            checkValid(that.endDate)
        }
        if (error && value) {
            that.setValue(options.range ? (that.endDate ? that.parse() : "") : that.parse())
        }
        var minMaxError;
        if (that.getDateTime(dateTime) > that.getDateTime(options.max)) {
            dateTime = options.dateTime = lay.extend({}, options.max);
            minMaxError = true
        } else {
            if (that.getDateTime(dateTime) < that.getDateTime(options.min)) {
                dateTime = options.dateTime = lay.extend({}, options.min);
                minMaxError = false
            }
        }
        if (options.range) {
            if (that.getDateTime(that.endDate) < that.getDateTime(options.min) || that.getDateTime(that.endDate) > that.getDateTime(options.max)) {
                that.endDate = lay.extend({}, options.max);
                minMaxError = true
            }
            that.startTime = {
                hours: options.dateTime.hours,
                minutes: options.dateTime.minutes,
                seconds: options.dateTime.seconds,
            };
            that.endTime = {
                hours: that.endDate.hours,
                minutes: that.endDate.minutes,
                seconds: that.endDate.seconds,
            }
        }
        minMaxError && that.setValue(that.parse()).hint("初始值" + lang.invalidDate + lang.formatError[1]);
        fn && fn();
        return that
    }
    ;
    Class.prototype.mark = function(td, YMD) {
        var that = this, mark, options = that.config;
        lay.each(options.mark, function(key, title) {
            var keys = key.split("-");
            if ((keys[0] == YMD[0] || keys[0] == 0) && (keys[1] == YMD[1] || keys[1] == 0) && keys[2] == YMD[2]) {
                mark = title || YMD[2]
            }
        });
        mark && td.html('<span class="laydate-day-mark">' + mark + "</span>");
        return that
    }
    ;
    Class.prototype.holidays = function(td, YMD) {
        var that = this;
        var options = that.config;
        var type = ["", "work"];
        if (layui.type(options.holidays) !== "array") {
            return that
        }
        lay.each(options.holidays, function(idx, item) {
            lay.each(item, function(i, dayStr) {
                if (dayStr === td.attr("lay-ymd")) {
                    td.html('<span class="laydate-day-holidays"' + (type[idx] ? ('type="' + type[idx] + '"') : "") + ">" + YMD[2] + "</span>")
                }
            })
        });
        return that
    }
    ;
    Class.prototype.limit = function(elem, date, index, time) {
        var that = this, options = that.config, timestrap = {}, dateTime = index > (time ? 0 : 41) ? that.endDate : options.dateTime, isOut, thisDateTime = lay.extend({}, dateTime, date || {});
        lay.each({
            now: thisDateTime,
            min: options.min,
            max: options.max
        }, function(key, item) {
            timestrap[key] = that.newDate(lay.extend({
                year: item.year,
                month: item.month,
                date: item.date
            }, function() {
                var hms = {};
                lay.each(time, function(i, keys) {
                    hms[keys] = item[keys]
                });
                return hms
            }())).getTime()
        });
        isOut = timestrap.now < timestrap.min || timestrap.now > timestrap.max;
        elem && elem[isOut ? "addClass" : "removeClass"](DISABLED);
        return isOut
    }
    ;
    Class.prototype.thisDateTime = function(index) {
        var that = this
          , options = that.config;
        return index ? that.endDate : options.dateTime
    }
    ;
    Class.prototype.calendar = function(value, index, type) {
        var that = this, options = that.config, index = index ? 1 : 0, dateTime = value || that.thisDateTime(index), thisDate = new Date(), startWeek, prevMaxDate, thisMaxDate, lang = that.lang(), isAlone = options.type !== "date" && options.type !== "datetime", tds = lay(that.table[index]).find("td"), elemYM = lay(that.elemHeader[index][2]).find("span");
        if (dateTime.year < LIMIT_YEAR[0]) {
            dateTime.year = LIMIT_YEAR[0],
            that.hint(lang.invalidDate)
        }
        if (dateTime.year > LIMIT_YEAR[1]) {
            dateTime.year = LIMIT_YEAR[1],
            that.hint(lang.invalidDate)
        }
        if (!that.firstDate) {
            that.firstDate = lay.extend({}, dateTime)
        }
        thisDate.setFullYear(dateTime.year, dateTime.month, 1);
        startWeek = (thisDate.getDay() + (7 - options.weekStart)) % 7;
        prevMaxDate = laydate.getEndDate(dateTime.month || 12, dateTime.year);
        thisMaxDate = laydate.getEndDate(dateTime.month + 1, dateTime.year);
        lay.each(tds, function(index_, item) {
            var YMD = [dateTime.year, dateTime.month]
              , st = 0;
            item = lay(item);
            item.removeAttr("class");
            if (index_ < startWeek) {
                st = prevMaxDate - startWeek + index_;
                item.addClass("laydate-day-prev");
                YMD = that.getAsYM(dateTime.year, dateTime.month, "sub")
            } else {
                if (index_ >= startWeek && index_ < thisMaxDate + startWeek) {
                    st = index_ - startWeek;
                    st + 1 === dateTime.date && item.addClass(THIS)
                } else {
                    st = index_ - thisMaxDate - startWeek;
                    item.addClass("laydate-day-next");
                    YMD = that.getAsYM(dateTime.year, dateTime.month)
                }
            }
            YMD[1]++;
            YMD[2] = st + 1;
            item.attr("lay-ymd", YMD.join("-")).html(YMD[2]);
            that.mark(item, YMD).holidays(item, YMD).limit(item, {
                year: YMD[0],
                month: YMD[1] - 1,
                date: YMD[2]
            }, index_)
        });
        lay(elemYM[0]).attr("lay-ym", dateTime.year + "-" + (dateTime.month + 1));
        lay(elemYM[1]).attr("lay-ym", dateTime.year + "-" + (dateTime.month + 1));
        if (options.lang === "cn") {
            lay(elemYM[0]).attr("lay-type", "year").html(dateTime.year + " 年");
            lay(elemYM[1]).attr("lay-type", "month").html((dateTime.month + 1) + " 月")
        } else {
            lay(elemYM[0]).attr("lay-type", "month").html(lang.month[dateTime.month]);
            lay(elemYM[1]).attr("lay-type", "year").html(dateTime.year)
        }
        if (isAlone) {
            if (options.range) {
                if (value) {
                    that.listYM = [[options.dateTime.year, options.dateTime.month + 1], [that.endDate.year, that.endDate.month + 1]];
                    that.list(options.type, 0).list(options.type, 1);
                    options.type === "time" ? that.setBtnStatus("时间", lay.extend({}, that.systemDate(), that.startTime), lay.extend({}, that.systemDate(), that.endTime)) : that.setBtnStatus(true)
                }
            } else {
                that.listYM = [[dateTime.year, dateTime.month + 1]];
                that.list(options.type, 0)
            }
        }
        if (options.range && type === "init" && !value) {
            that.calendar(that.endDate, 1)
        }
        if (!options.range) {
            that.limit(lay(that.footer).find(ELEM_CONFIRM), null, 0, ["hours", "minutes", "seconds"])
        }
        that.setBtnStatus();
        return that
    }
    ;
    Class.prototype.list = function(type, index) {
        var that = this
          , options = that.config
          , dateTime = options.dateTime
          , lang = that.lang()
          , isAlone = options.range && options.type !== "date" && options.type !== "datetime"
          , ul = lay.elem("ul", {
            "class": ELEM_LIST + " " + ({
                year: "laydate-year-list",
                month: "laydate-month-list",
                time: "laydate-time-list"
            })[type]
        })
          , elemHeader = that.elemHeader[index]
          , elemYM = lay(elemHeader[2]).find("span")
          , elemCont = that.elemCont[index || 0]
          , haveList = lay(elemCont).find("." + ELEM_LIST)[0]
          , isCN = options.lang === "cn"
          , text = isCN ? "年" : ""
          , listYM = that.listYM[index] || {}
          , hms = ["hours", "minutes", "seconds"]
          , startEnd = ["startTime", "endTime"][index];
        if (listYM[0] < 1) {
            listYM[0] = 1
        }
        if (type === "year") {
            var yearNum, startY = yearNum = listYM[0] - 7;
            if (startY < 1) {
                startY = yearNum = 1
            }
            lay.each(new Array(15), function(i) {
                var li = lay.elem("li", {
                    "lay-ym": yearNum
                })
                  , ymd = {
                    year: yearNum,
                    month: 0,
                    date: 1
                };
                yearNum == listYM[0] && lay(li).addClass(THIS);
                li.innerHTML = yearNum + text;
                ul.appendChild(li);
                that.limit(lay(li), ymd, index);
                yearNum++
            });
            lay(elemYM[isCN ? 0 : 1]).attr("lay-ym", (yearNum - 8) + "-" + listYM[1]).html((startY + text) + " - " + (yearNum - 1 + text))
        } else {
            if (type === "month") {
                lay.each(new Array(12), function(i) {
                    var li = lay.elem("li", {
                        "lay-ym": i
                    })
                      , ymd = {
                        year: listYM[0],
                        month: i,
                        date: 1
                    };
                    i + 1 == listYM[1] && lay(li).addClass(THIS);
                    li.innerHTML = lang.month[i] + (isCN ? "月" : "");
                    ul.appendChild(li);
                    that.limit(lay(li), ymd, index)
                });
                lay(elemYM[isCN ? 0 : 1]).attr("lay-ym", listYM[0] + "-" + listYM[1]).html(listYM[0] + text)
            } else {
                if (type === "time") {
                    var setTimeStatus = function() {
                        lay(ul).find("ol").each(function(i, ol) {
                            lay(ol).find("li").each(function(ii, li) {
                                that.limit(lay(li), [{
                                    hours: ii
                                }, {
                                    hours: that[startEnd].hours,
                                    minutes: ii
                                }, {
                                    hours: that[startEnd].hours,
                                    minutes: that[startEnd].minutes,
                                    seconds: ii
                                }][i], index, [["hours"], ["hours", "minutes"], ["hours", "minutes", "seconds"]][i])
                            })
                        });
                        if (!options.range) {
                            that.limit(lay(that.footer).find(ELEM_CONFIRM), that[startEnd], 0, ["hours", "minutes", "seconds"])
                        }
                    };
                    if (options.range) {
                        if (!that[startEnd]) {
                            that[startEnd] = startEnd === "startTime" ? dateTime : that.endDate
                        }
                    } else {
                        that[startEnd] = dateTime
                    }
                    lay.each([24, 60, 60], function(i, item) {
                        var li = lay.elem("li")
                          , childUL = ["<p>" + lang.time[i] + "</p><ol>"];
                        lay.each(new Array(item), function(ii) {
                            childUL.push("<li" + (that[startEnd][hms[i]] === ii ? ' class="' + THIS + '"' : "") + ">" + lay.digit(ii, 2) + "</li>")
                        });
                        li.innerHTML = childUL.join("") + "</ol>";
                        ul.appendChild(li)
                    });
                    setTimeStatus()
                }
            }
        }
        if (haveList) {
            elemCont.removeChild(haveList)
        }
        elemCont.appendChild(ul);
        if (type === "year" || type === "month") {
            lay(that.elemMain[index]).addClass("laydate-ym-show");
            lay(ul).find("li").on("click", function() {
                var ym = lay(this).attr("lay-ym") | 0;
                if (lay(this).hasClass(DISABLED)) {
                    return
                }
                if (index === 0) {
                    dateTime[type] = ym;
                    that.limit(lay(that.footer).find(ELEM_CONFIRM), null, 0)
                } else {
                    that.endDate[type] = ym
                }
                var isYearOrMonth = options.type === "year" || options.type === "month";
                if (isYearOrMonth) {
                    lay(ul).find("." + THIS).removeClass(THIS);
                    lay(this).addClass(THIS);
                    if (options.type === "month" && type === "year") {
                        that.listYM[index][0] = ym;
                        isAlone && ((index ? that.endDate : dateTime).year = ym);
                        that.list("month", index)
                    }
                } else {
                    that.checkDate("limit").calendar(null, index);
                    that.closeList()
                }
                that.setBtnStatus();
                if (!options.range) {
                    if ((options.type === "month" && type === "month") || (options.type === "year" && type === "year")) {
                        that.setValue(that.parse()).remove().done()
                    }
                }
                that.done(null, "change");
                lay(that.footer).find("." + ELEM_TIME_BTN).removeClass(DISABLED)
            })
        } else {
            var span = lay.elem("span", {
                "class": ELEM_TIME_TEXT
            })
              , scroll = function() {
                lay(ul).find("ol").each(function(i) {
                    var ol = this
                      , li = lay(ol).find("li");
                    ol.scrollTop = 30 * (that[startEnd][hms[i]] - 2);
                    if (ol.scrollTop <= 0) {
                        li.each(function(ii, item) {
                            if (!lay(this).hasClass(DISABLED)) {
                                ol.scrollTop = 30 * (ii - 2);
                                return true
                            }
                        })
                    }
                })
            }
              , haveSpan = lay(elemHeader[2]).find("." + ELEM_TIME_TEXT);
            scroll();
            span.innerHTML = options.range ? [lang.startTime, lang.endTime][index] : lang.timeTips;
            lay(that.elemMain[index]).addClass("laydate-time-show");
            if (haveSpan[0]) {
                haveSpan.remove()
            }
            elemHeader[2].appendChild(span);
            lay(ul).find("ol").each(function(i) {
                var ol = this;
                lay(ol).find("li").on("click", function() {
                    var value = this.innerHTML | 0;
                    if (lay(this).hasClass(DISABLED)) {
                        return
                    }
                    if (options.range) {
                        that[startEnd][hms[i]] = value
                    } else {
                        dateTime[hms[i]] = value
                    }
                    lay(ol).find("." + THIS).removeClass(THIS);
                    lay(this).addClass(THIS);
                    setTimeStatus();
                    scroll();
                    (that.endDate || options.type === "time") && that.done(null, "change");
                    that.setBtnStatus()
                })
            })
        }
        return that
    }
    ;
    Class.prototype.listYM = [];
    Class.prototype.closeList = function() {
        var that = this
          , options = that.config;
        lay.each(that.elemCont, function(index, item) {
            lay(this).find("." + ELEM_LIST).remove();
            lay(that.elemMain[index]).removeClass("laydate-ym-show laydate-time-show")
        });
        lay(that.elem).find("." + ELEM_TIME_TEXT).remove()
    }
    ;
    Class.prototype.setBtnStatus = function(tips, start, end) {
        var that = this, options = that.config, lang = that.lang(), isOut, elemBtn = lay(that.footer).find(ELEM_CONFIRM);
        if (options.range && options.type !== "time") {
            start = start || options.dateTime;
            end = end || that.endDate;
            isOut = that.newDate(start).getTime() > that.newDate(end).getTime();
            (that.limit(null, start) || that.limit(null, end)) ? elemBtn.addClass(DISABLED) : elemBtn[isOut ? "addClass" : "removeClass"](DISABLED);
            if (tips && isOut) {
                that.hint(typeof tips === "string" ? lang.timeout.replace(/日期/g, tips) : lang.timeout)
            }
        }
    }
    ;
    Class.prototype.parse = function(state, date) {
        var that = this
          , options = that.config
          , dateTime = date || (state == "end" ? lay.extend({}, that.endDate, that.endTime) : (options.range ? lay.extend({}, options.dateTime, that.startTime) : options.dateTime))
          , format = laydate.parse(dateTime, that.format, 1);
        if (options.range && state === undefined) {
            return format + " " + that.rangeStr + " " + that.parse("end")
        }
        return format
    }
    ;
    Class.prototype.newDate = function(dateTime) {
        dateTime = dateTime || {};
        return new Date(dateTime.year || 1,dateTime.month || 0,dateTime.date || 1,dateTime.hours || 0,dateTime.minutes || 0,dateTime.seconds || 0)
    }
    ;
    Class.prototype.getDateTime = function(obj) {
        return this.newDate(obj).getTime()
    }
    ;
    Class.prototype.setValue = function(value) {
        var that = this
          , options = that.config
          , elem = that.bindElem || options.elem[0];
        if (options.position === "static") {
            return that
        }
        value = value || "";
        if (that.isInput(elem)) {
            lay(elem).val(value)
        } else {
            var rangeElem = that.rangeElem;
            if (rangeElem) {
                if (layui.type(value) !== "array") {
                    value = value.split(" " + that.rangeStr + " ")
                }
                rangeElem[0].val(value[0] || "");
                rangeElem[1].val(value[1] || "")
            } else {
                if (lay(elem).find("*").length === 0) {
                    lay(elem).html(value)
                }
                lay(elem).attr("lay-date", value)
            }
        }
        return that
    }
    ;
    Class.prototype.preview = function() {
        var that = this
          , options = that.config;
        if (!options.isPreview) {
            return
        }
        var elemPreview = lay(that.elem).find("." + ELEM_PREVIEW)
          , value = options.range ? (that.endDate ? that.parse() : "") : that.parse();
        elemPreview.html(value).css({
            "color": "#5FB878"
        });
        setTimeout(function() {
            elemPreview.css({
                "color": "#666"
            })
        }, 300)
    }
    ;
    Class.prototype.done = function(param, type) {
        var that = this
          , options = that.config
          , start = lay.extend({}, lay.extend(options.dateTime, that.startTime))
          , end = lay.extend({}, lay.extend(that.endDate, that.endTime));
        lay.each([start, end], function(i, item) {
            if (!("month"in item)) {
                return
            }
            lay.extend(item, {
                month: item.month + 1
            })
        });
        that.preview();
        param = param || [that.parse(), start, end];
        typeof options[type || "done"] === "function" && options[type || "done"].apply(options, param);
        return that
    }
    ;
    Class.prototype.choose = function(td, index) {
        var that = this
          , options = that.config
          , dateTime = that.thisDateTime(index)
          , tds = lay(that.elem).find("td")
          , YMD = td.attr("lay-ymd").split("-");
        YMD = {
            year: YMD[0] | 0,
            month: (YMD[1] | 0) - 1,
            date: YMD[2] | 0
        };
        if (td.hasClass(DISABLED)) {
            return
        }
        lay.extend(dateTime, YMD);
        if (options.range) {
            lay.each(["startTime", "endTime"], function(i, item) {
                that[item] = that[item] || {
                    hours: i ? 23 : 0,
                    minutes: i ? 59 : 0,
                    seconds: i ? 59 : 0
                };
                if (index === i) {
                    if (that.getDateTime(lay.extend({}, dateTime, that[item])) < that.getDateTime(options.min)) {
                        that[item] = {
                            hours: options.min.hours,
                            minutes: options.min.minutes,
                            seconds: options.min.seconds
                        };
                        lay.extend(dateTime, that[item])
                    } else {
                        if (that.getDateTime(lay.extend({}, dateTime, that[item])) > that.getDateTime(options.max)) {
                            that[item] = {
                                hours: options.max.hours,
                                minutes: options.max.minutes,
                                seconds: options.max.seconds
                            };
                            lay.extend(dateTime, that[item])
                        }
                    }
                }
            });
            that.calendar(null, index).done(null, "change")
        } else {
            if (options.position === "static") {
                that.calendar().done().done(null, "change")
            } else {
                if (options.type === "date") {
                    that.setValue(that.parse()).remove().done()
                } else {
                    if (options.type === "datetime") {
                        that.calendar().done(null, "change")
                    }
                }
            }
        }
    }
    ;
    Class.prototype.tool = function(btn, type) {
        var that = this
          , options = that.config
          , lang = that.lang()
          , dateTime = options.dateTime
          , isStatic = options.position === "static"
          , active = {
            datetime: function() {
                if (lay(btn).hasClass(DISABLED)) {
                    return
                }
                that.list("time", 0);
                options.range && that.list("time", 1);
                lay(btn).attr("lay-type", "date").html(that.lang().dateTips)
            },
            date: function() {
                that.closeList();
                lay(btn).attr("lay-type", "datetime").html(that.lang().timeTips)
            },
            clear: function() {
                isStatic && (lay.extend(dateTime, that.firstDate),
                that.calendar());
                options.range && (delete options.dateTime,
                delete that.endDate,
                delete that.startTime,
                delete that.endTime);
                that.setValue("").remove();
                that.done(["", {}, {}])
            },
            now: function() {
                var thisDate = new Date();
                lay.extend(dateTime, that.systemDate(), {
                    hours: thisDate.getHours(),
                    minutes: thisDate.getMinutes(),
                    seconds: thisDate.getSeconds()
                });
                that.setValue(that.parse()).remove();
                isStatic && that.calendar();
                that.done()
            },
            confirm: function() {
                if (options.range) {
                    if (lay(btn).hasClass(DISABLED)) {
                        return that.hint(options.type === "time" ? lang.timeout.replace(/日期/g, "时间") : lang.timeout)
                    }
                } else {
                    if (lay(btn).hasClass(DISABLED)) {
                        return that.hint(lang.invalidDate)
                    }
                }
                that.setValue(that.parse()).remove();
                that.done()
            }
        };
        active[type] && active[type]()
    }
    ;
    Class.prototype.change = function(index) {
        var that = this
          , options = that.config
          , dateTime = that.thisDateTime(index)
          , isAlone = options.range && (options.type === "year" || options.type === "month")
          , elemCont = that.elemCont[index || 0]
          , listYM = that.listYM[index]
          , addSubYeay = function(type) {
            var isYear = lay(elemCont).find(".laydate-year-list")[0]
              , isMonth = lay(elemCont).find(".laydate-month-list")[0];
            if (isYear) {
                listYM[0] = type ? listYM[0] - 15 : listYM[0] + 15;
                that.list("year", index)
            }
            if (isMonth) {
                type ? listYM[0]-- : listYM[0]++;
                that.list("month", index)
            }
            if (isYear || isMonth) {
                lay.extend(dateTime, {
                    year: listYM[0]
                });
                if (isAlone) {
                    dateTime.year = listYM[0]
                }
                options.range || that.done(null, "change");
                options.range || that.limit(lay(that.footer).find(ELEM_CONFIRM), {
                    year: listYM[0]
                })
            }
            that.setBtnStatus();
            return isYear || isMonth
        };
        return {
            prevYear: function() {
                if (addSubYeay("sub")) {
                    return
                }
                dateTime.year--;
                that.checkDate("limit").calendar(null, index);
                that.done(null, "change")
            },
            prevMonth: function() {
                var YM = that.getAsYM(dateTime.year, dateTime.month, "sub");
                lay.extend(dateTime, {
                    year: YM[0],
                    month: YM[1]
                });
                that.checkDate("limit").calendar(null, index);
                that.done(null, "change")
            },
            nextMonth: function() {
                var YM = that.getAsYM(dateTime.year, dateTime.month);
                lay.extend(dateTime, {
                    year: YM[0],
                    month: YM[1]
                });
                that.checkDate("limit").calendar(null, index);
                that.done(null, "change")
            },
            nextYear: function() {
                if (addSubYeay()) {
                    return
                }
                dateTime.year++;
                that.checkDate("limit").calendar(null, index);
                that.done(null, "change")
            }
        }
    }
    ;
    Class.prototype.changeEvent = function() {
        var that = this
          , options = that.config;
        lay(that.elem).on("click", function(e) {
            lay.stope(e)
        }).on("mousedown", function(e) {
            lay.stope(e)
        });
        lay.each(that.elemHeader, function(i, header) {
            lay(header[0]).on("click", function(e) {
                that.change(i).prevYear()
            });
            lay(header[1]).on("click", function(e) {
                that.change(i).prevMonth()
            });
            lay(header[2]).find("span").on("click", function(e) {
                var othis = lay(this)
                  , layYM = othis.attr("lay-ym")
                  , layType = othis.attr("lay-type");
                if (!layYM) {
                    return
                }
                layYM = layYM.split("-");
                that.listYM[i] = [layYM[0] | 0, layYM[1] | 0];
                that.list(layType, i);
                lay(that.footer).find("." + ELEM_TIME_BTN).addClass(DISABLED)
            });
            lay(header[3]).on("click", function(e) {
                that.change(i).nextMonth()
            });
            lay(header[4]).on("click", function(e) {
                that.change(i).nextYear()
            })
        });
        lay.each(that.table, function(i, table) {
            var tds = lay(table).find("td");
            tds.on("click", function() {
                that.choose(lay(this), i)
            })
        });
        lay(that.footer).find("span").on("click", function() {
            var type = lay(this).attr("lay-type");
            that.tool(this, type)
        })
    }
    ;
    Class.prototype.isInput = function(elem) {
        return /input|textarea/.test(elem.tagName.toLocaleLowerCase()) || /INPUT|TEXTAREA/.test(elem.tagName)
    }
    ;
    Class.prototype.events = function() {
        var that = this
          , options = that.config
          , showEvent = function(elem, bind) {
            elem.on(options.trigger, function() {
                if (laydate.thisId === options.id) {
                    return
                }
                bind && (that.bindElem = this);
                that.render()
            })
        };
        if (!options.elem[0] || options.elem[0].eventHandler) {
            return
        }
        showEvent(options.elem, "bind");
        showEvent(options.eventElem);
        options.elem[0].eventHandler = true
    }
    ;
    thisModule.that = {};
    thisModule.getThis = function(id) {
        var that = thisModule.that[id];
        if (!that && isLayui) {
            layui.hint().error(id ? (MOD_NAME + " instance with ID '" + id + "' not found") : "ID argument required")
        }
        return that
    }
    ;
    ready.run = function(lay) {
        lay(document).on("mousedown", function(e) {
            if (!laydate.thisId) {
                return
            }
            var that = thisModule.getThis(laydate.thisId);
            if (!that) {
                return
            }
            var options = that.config;
            if (e.target === options.elem[0] || e.target === options.eventElem[0] || e.target === lay(options.closeStop)[0]) {
                return
            }
            that.remove()
        }).on("keydown", function(e) {
            if (!laydate.thisId) {
                return
            }
            var that = thisModule.getThis(laydate.thisId);
            if (!that) {
                return
            }
            if (that.config.position === "static") {
                return
            }
            if (e.keyCode === 13) {
                if (lay("#" + that.elemID)[0] && that.elemID === Class.thisElemDate) {
                    e.preventDefault();
                    lay(that.footer).find(ELEM_CONFIRM)[0].click()
                }
            }
        });
        lay(window).on("resize", function() {
            if (!laydate.thisId) {
                return
            }
            var that = thisModule.getThis(laydate.thisId);
            if (!that) {
                return
            }
            if (!that.elem || !lay(ELEM)[0]) {
                return false
            }
            that.position()
        })
    }
    ;
    laydate.render = function(options) {
        var inst = new Class(options);
        return thisModule.call(inst)
    }
    ;
    laydate.parse = function(dateTime, format, one) {
        dateTime = dateTime || {};
        if (typeof format === "string") {
            format = thisModule.formatArr(format)
        }
        format = (format || []).concat();
        lay.each(format, function(i, item) {
            if (/yyyy|y/.test(item)) {
                format[i] = lay.digit(dateTime.year, item.length)
            } else {
                if (/MM|M/.test(item)) {
                    format[i] = lay.digit(dateTime.month + (one || 0), item.length)
                } else {
                    if (/dd|d/.test(item)) {
                        format[i] = lay.digit(dateTime.date, item.length)
                    } else {
                        if (/HH|H/.test(item)) {
                            format[i] = lay.digit(dateTime.hours, item.length)
                        } else {
                            if (/mm|m/.test(item)) {
                                format[i] = lay.digit(dateTime.minutes, item.length)
                            } else {
                                if (/ss|s/.test(item)) {
                                    format[i] = lay.digit(dateTime.seconds, item.length)
                                }
                            }
                        }
                    }
                }
            }
        });
        return format.join("")
    }
    ;
    laydate.getEndDate = function(month, year) {
        var thisDate = new Date();
        thisDate.setFullYear(year || thisDate.getFullYear(), month || (thisDate.getMonth() + 1), 1);
        return new Date(thisDate.getTime() - 1000 * 60 * 60 * 24).getDate()
    }
    ;
    laydate.close = function(id) {
        var that = thisModule.getThis(id || laydate.thisId);
        if (!that) {
            return
        }
        return that.remove()
    }
    ;
    isLayui ? (laydate.ready(),
    layui.define("lay", function(exports) {
        laydate.path = layui.cache.dir;
        ready.run(lay);
        exports(MOD_NAME, laydate)
    })) : ((typeof define === "function" && define.amd) ? define(function() {
        ready.run(lay);
        return laydate
    }) : function() {
        laydate.ready();
        ready.run(window.lay);
        window.laydate = laydate
    }())
}(window, window.document);
