if (function(e, t) {
    "object" == typeof module && "object" == typeof module.exports ? module.exports = e.document ? t(e, !0) : function(e) {
        if (!e.document) throw new Error("jQuery requires a window with a document");
        return t(e)
    } : t(e)
}("undefined" != typeof window ? window : this, function(e, t) {
    function n(e) {
        var t = !!e && "length" in e && e.length,
            n = fe.type(e);
        return "function" !== n && !fe.isWindow(e) && ("array" === n || 0 === t || "number" == typeof t && t > 0 && t - 1 in e)
    }

    function i(e, t, n) {
        if (fe.isFunction(t)) return fe.grep(e, function(e, i) {
            return !!t.call(e, i, e) !== n
        });
        if (t.nodeType) return fe.grep(e, function(e) {
            return e === t !== n
        });
        if ("string" == typeof t) {
            if (Ce.test(t)) return fe.filter(t, e, n);
            t = fe.filter(t, e)
        }
        return fe.grep(e, function(e) {
            return fe.inArray(e, t) > -1 !== n
        })
    }

    function o(e, t) {
        do e = e[t]; while (e && 1 !== e.nodeType);
        return e
    }

    function s(e) {
        var t = {};
        return fe.each(e.match(Ne) || [], function(e, n) {
            t[n] = !0
        }), t
    }

    function r() {
        ie.addEventListener ? (ie.removeEventListener("DOMContentLoaded", a), e.removeEventListener("load", a)) : (ie.detachEvent("onreadystatechange", a), e.detachEvent("onload", a))
    }

    function a() {
        (ie.addEventListener || "load" === e.event.type || "complete" === ie.readyState) && (r(), fe.ready())
    }

    function l(e, t, n) {
        if (void 0 === n && 1 === e.nodeType) {
            var i = "data-" + t.replace(Ue, "-$1").toLowerCase();
            if (n = e.getAttribute(i), "string" == typeof n) {
                try {
                    n = "true" === n || "false" !== n && ("null" === n ? null : +n + "" === n ? +n : $e.test(n) ? fe.parseJSON(n) : n)
                } catch (o) {}
                fe.data(e, t, n)
            } else n = void 0
        }
        return n
    }

    function c(e) {
        var t;
        for (t in e)
            if (("data" !== t || !fe.isEmptyObject(e[t])) && "toJSON" !== t) return !1;
        return !0
    }

    function u(e, t, n, i) {
        if (Oe(e)) {
            var o, s, r = fe.expando,
                a = e.nodeType,
                l = a ? fe.cache : e,
                c = a ? e[r] : e[r] && r;
            if (c && l[c] && (i || l[c].data) || void 0 !== n || "string" != typeof t) return c || (c = a ? e[r] = ne.pop() || fe.guid++ : r), l[c] || (l[c] = a ? {} : {
                toJSON: fe.noop
            }), "object" != typeof t && "function" != typeof t || (i ? l[c] = fe.extend(l[c], t) : l[c].data = fe.extend(l[c].data, t)), s = l[c], i || (s.data || (s.data = {}), s = s.data), void 0 !== n && (s[fe.camelCase(t)] = n), "string" == typeof t ? (o = s[t], null == o && (o = s[fe.camelCase(t)])) : o = s, o
        }
    }

    function d(e, t, n) {
        if (Oe(e)) {
            var i, o, s = e.nodeType,
                r = s ? fe.cache : e,
                a = s ? e[fe.expando] : fe.expando;
            if (r[a]) {
                if (t && (i = n ? r[a] : r[a].data)) {
                    fe.isArray(t) ? t = t.concat(fe.map(t, fe.camelCase)) : t in i ? t = [t] : (t = fe.camelCase(t), t = t in i ? [t] : t.split(" ")), o = t.length;
                    for (; o--;) delete i[t[o]];
                    if (n ? !c(i) : !fe.isEmptyObject(i)) return
                }(n || (delete r[a].data, c(r[a]))) && (s ? fe.cleanData([e], !0) : de.deleteExpando || r != r.window ? delete r[a] : r[a] = void 0)
            }
        }
    }

    function h(e, t, n, i) {
        var o, s = 1,
            r = 20,
            a = i ? function() {
                return i.cur()
            } : function() {
                return fe.css(e, t, "")
            },
            l = a(),
            c = n && n[3] || (fe.cssNumber[t] ? "" : "px"),
            u = (fe.cssNumber[t] || "px" !== c && +l) && Le.exec(fe.css(e, t));
        if (u && u[3] !== c) {
            c = c || u[3], n = n || [], u = +l || 1;
            do s = s || ".5", u /= s, fe.style(e, t, u + c); while (s !== (s = a() / l) && 1 !== s && --r)
        }
        return n && (u = +u || +l || 0, o = n[1] ? u + (n[1] + 1) * n[2] : +n[2], i && (i.unit = c, i.start = u, i.end = o)), o
    }

    function f(e) {
        var t = Ve.split("|"),
            n = e.createDocumentFragment();
        if (n.createElement)
            for (; t.length;) n.createElement(t.pop());
        return n
    }

    function p(e, t) {
        var n, i, o = 0,
            s = "undefined" != typeof e.getElementsByTagName ? e.getElementsByTagName(t || "*") : "undefined" != typeof e.querySelectorAll ? e.querySelectorAll(t || "*") : void 0;
        if (!s)
            for (s = [], n = e.childNodes || e; null != (i = n[o]); o++)!t || fe.nodeName(i, t) ? s.push(i) : fe.merge(s, p(i, t));
        return void 0 === t || t && fe.nodeName(e, t) ? fe.merge([e], s) : s
    }

    function m(e, t) {
        for (var n, i = 0; null != (n = e[i]); i++) fe._data(n, "globalEval", !t || fe._data(t[i], "globalEval"))
    }

    function g(e) {
        Pe.test(e.type) && (e.defaultChecked = e.checked)
    }

    function v(e, t, n, i, o) {
        for (var s, r, a, l, c, u, d, h = e.length, v = f(t), y = [], b = 0; b < h; b++)
            if (r = e[b], r || 0 === r)
                if ("object" === fe.type(r)) fe.merge(y, r.nodeType ? [r] : r);
                else if (ze.test(r)) {
            for (l = l || v.appendChild(t.createElement("div")), c = (_e.exec(r) || ["", ""])[1].toLowerCase(), d = We[c] || We._default, l.innerHTML = d[1] + fe.htmlPrefilter(r) + d[2], s = d[0]; s--;) l = l.lastChild;
            if (!de.leadingWhitespace && Be.test(r) && y.push(t.createTextNode(Be.exec(r)[0])), !de.tbody)
                for (r = "table" !== c || Ye.test(r) ? "<table>" !== d[1] || Ye.test(r) ? 0 : l : l.firstChild, s = r && r.childNodes.length; s--;) fe.nodeName(u = r.childNodes[s], "tbody") && !u.childNodes.length && r.removeChild(u);
            for (fe.merge(y, l.childNodes), l.textContent = ""; l.firstChild;) l.removeChild(l.firstChild);
            l = v.lastChild
        } else y.push(t.createTextNode(r));
        for (l && v.removeChild(l), de.appendChecked || fe.grep(p(y, "input"), g), b = 0; r = y[b++];)
            if (i && fe.inArray(r, i) > -1) o && o.push(r);
            else if (a = fe.contains(r.ownerDocument, r), l = p(v.appendChild(r), "script"), a && m(l), n)
            for (s = 0; r = l[s++];) qe.test(r.type || "") && n.push(r);
        return l = null, v
    }

    function y() {
        return !0
    }

    function b() {
        return !1
    }

    function w() {
        try {
            return ie.activeElement
        } catch (e) {}
    }

    function T(e, t, n, i, o, s) {
        var r, a;
        if ("object" == typeof t) {
            "string" != typeof n && (i = i || n, n = void 0);
            for (a in t) T(e, a, n, i, t[a], s);
            return e
        }
        if (null == i && null == o ? (o = n, i = n = void 0) : null == o && ("string" == typeof n ? (o = i, i = void 0) : (o = i, i = n, n = void 0)), o === !1) o = b;
        else if (!o) return e;
        return 1 === s && (r = o, o = function(e) {
            return fe().off(e), r.apply(this, arguments)
        }, o.guid = r.guid || (r.guid = fe.guid++)), e.each(function() {
            fe.event.add(this, t, o, i, n)
        })
    }

    function x(e, t) {
        return fe.nodeName(e, "table") && fe.nodeName(11 !== t.nodeType ? t : t.firstChild, "tr") ? e.getElementsByTagName("tbody")[0] || e.appendChild(e.ownerDocument.createElement("tbody")) : e
    }

    function C(e) {
        return e.type = (null !== fe.find.attr(e, "type")) + "/" + e.type, e
    }

    function D(e) {
        var t = ot.exec(e.type);
        return t ? e.type = t[1] : e.removeAttribute("type"), e
    }

    function k(e, t) {
        if (1 === t.nodeType && fe.hasData(e)) {
            var n, i, o, s = fe._data(e),
                r = fe._data(t, s),
                a = s.events;
            if (a) {
                delete r.handle, r.events = {};
                for (n in a)
                    for (i = 0, o = a[n].length; i < o; i++) fe.event.add(t, n, a[n][i])
            }
            r.data && (r.data = fe.extend({}, r.data))
        }
    }

    function S(e, t) {
        var n, i, o;
        if (1 === t.nodeType) {
            if (n = t.nodeName.toLowerCase(), !de.noCloneEvent && t[fe.expando]) {
                o = fe._data(t);
                for (i in o.events) fe.removeEvent(t, i, o.handle);
                t.removeAttribute(fe.expando)
            }
            "script" === n && t.text !== e.text ? (C(t).text = e.text, D(t)) : "object" === n ? (t.parentNode && (t.outerHTML = e.outerHTML), de.html5Clone && e.innerHTML && !fe.trim(t.innerHTML) && (t.innerHTML = e.innerHTML)) : "input" === n && Pe.test(e.type) ? (t.defaultChecked = t.checked = e.checked, t.value !== e.value && (t.value = e.value)) : "option" === n ? t.defaultSelected = t.selected = e.defaultSelected : "input" !== n && "textarea" !== n || (t.defaultValue = e.defaultValue)
        }
    }

    function E(e, t, n, i) {
        t = se.apply([], t);
        var o, s, r, a, l, c, u = 0,
            d = e.length,
            h = d - 1,
            f = t[0],
            m = fe.isFunction(f);
        if (m || d > 1 && "string" == typeof f && !de.checkClone && it.test(f)) return e.each(function(o) {
            var s = e.eq(o);
            m && (t[0] = f.call(this, o, s.html())), E(s, t, n, i)
        });
        if (d && (c = v(t, e[0].ownerDocument, !1, e, i), o = c.firstChild, 1 === c.childNodes.length && (c = o), o || i)) {
            for (a = fe.map(p(c, "script"), C), r = a.length; u < d; u++) s = c, u !== h && (s = fe.clone(s, !0, !0), r && fe.merge(a, p(s, "script"))), n.call(e[u], s, u);
            if (r)
                for (l = a[a.length - 1].ownerDocument, fe.map(a, D), u = 0; u < r; u++) s = a[u], qe.test(s.type || "") && !fe._data(s, "globalEval") && fe.contains(l, s) && (s.src ? fe._evalUrl && fe._evalUrl(s.src) : fe.globalEval((s.text || s.textContent || s.innerHTML || "").replace(st, "")));
            c = o = null
        }
        return e
    }

    function M(e, t, n) {
        for (var i, o = t ? fe.filter(t, e) : e, s = 0; null != (i = o[s]); s++) n || 1 !== i.nodeType || fe.cleanData(p(i)), i.parentNode && (n && fe.contains(i.ownerDocument, i) && m(p(i, "script")), i.parentNode.removeChild(i));
        return e
    }

    function N(e, t) {
        var n = fe(t.createElement(e)).appendTo(t.body),
            i = fe.css(n[0], "display");
        return n.detach(), i
    }

    function A(e) {
        var t = ie,
            n = ct[e];
        return n || (n = N(e, t), "none" !== n && n || (lt = (lt || fe("<iframe frameborder='0' width='0' height='0'/>")).appendTo(t.documentElement), t = (lt[0].contentWindow || lt[0].contentDocument).document, t.write(), t.close(), n = N(e, t), lt.detach()), ct[e] = n), n
    }

    function I(e, t) {
        return {
            get: function() {
                return e() ? void delete this.get : (this.get = t).apply(this, arguments)
            }
        }
    }

    function O(e) {
        if (e in Dt) return e;
        for (var t = e.charAt(0).toUpperCase() + e.slice(1), n = Ct.length; n--;)
            if (e = Ct[n] + t, e in Dt) return e
    }

    function $(e, t) {
        for (var n, i, o, s = [], r = 0, a = e.length; r < a; r++) i = e[r], i.style && (s[r] = fe._data(i, "olddisplay"), n = i.style.display, t ? (s[r] || "none" !== n || (i.style.display = ""), "" === i.style.display && Fe(i) && (s[r] = fe._data(i, "olddisplay", A(i.nodeName)))) : (o = Fe(i), (n && "none" !== n || !o) && fe._data(i, "olddisplay", o ? n : fe.css(i, "display"))));
        for (r = 0; r < a; r++) i = e[r], i.style && (t && "none" !== i.style.display && "" !== i.style.display || (i.style.display = t ? s[r] || "" : "none"));
        return e
    }

    function U(e, t, n) {
        var i = wt.exec(t);
        return i ? Math.max(0, i[1] - (n || 0)) + (i[2] || "px") : t
    }

    function j(e, t, n, i, o) {
        for (var s = n === (i ? "border" : "content") ? 4 : "width" === t ? 1 : 0, r = 0; s < 4; s += 2) "margin" === n && (r += fe.css(e, n + He[s], !0, o)), i ? ("content" === n && (r -= fe.css(e, "padding" + He[s], !0, o)), "margin" !== n && (r -= fe.css(e, "border" + He[s] + "Width", !0, o))) : (r += fe.css(e, "padding" + He[s], !0, o), "padding" !== n && (r += fe.css(e, "border" + He[s] + "Width", !0, o)));
        return r
    }

    function L(e, t, n) {
        var i = !0,
            o = "width" === t ? e.offsetWidth : e.offsetHeight,
            s = pt(e),
            r = de.boxSizing && "border-box" === fe.css(e, "boxSizing", !1, s);
        if (o <= 0 || null == o) {
            if (o = mt(e, t, s), (o < 0 || null == o) && (o = e.style[t]), dt.test(o)) return o;
            i = r && (de.boxSizingReliable() || o === e.style[t]), o = parseFloat(o) || 0
        }
        return o + j(e, t, n || (r ? "border" : "content"), i, s) + "px"
    }

    function H(e, t, n, i, o) {
        return new H.prototype.init(e, t, n, i, o)
    }

    function F() {
        return e.setTimeout(function() {
            kt = void 0
        }), kt = fe.now()
    }

    function R(e, t) {
        var n, i = {
                height: e
            },
            o = 0;
        for (t = t ? 1 : 0; o < 4; o += 2 - t) n = He[o], i["margin" + n] = i["padding" + n] = e;
        return t && (i.opacity = i.width = e), i
    }

    function P(e, t, n) {
        for (var i, o = (B.tweeners[t] || []).concat(B.tweeners["*"]), s = 0, r = o.length; s < r; s++)
            if (i = o[s].call(n, t, e)) return i
    }

    function _(e, t, n) {
        var i, o, s, r, a, l, c, u, d = this,
            h = {},
            f = e.style,
            p = e.nodeType && Fe(e),
            m = fe._data(e, "fxshow");
        n.queue || (a = fe._queueHooks(e, "fx"), null == a.unqueued && (a.unqueued = 0, l = a.empty.fire, a.empty.fire = function() {
            a.unqueued || l()
        }), a.unqueued++, d.always(function() {
            d.always(function() {
                a.unqueued--, fe.queue(e, "fx").length || a.empty.fire()
            })
        })), 1 === e.nodeType && ("height" in t || "width" in t) && (n.overflow = [f.overflow, f.overflowX, f.overflowY], c = fe.css(e, "display"), u = "none" === c ? fe._data(e, "olddisplay") || A(e.nodeName) : c, "inline" === u && "none" === fe.css(e, "float") && (de.inlineBlockNeedsLayout && "inline" !== A(e.nodeName) ? f.zoom = 1 : f.display = "inline-block")), n.overflow && (f.overflow = "hidden", de.shrinkWrapBlocks() || d.always(function() {
            f.overflow = n.overflow[0], f.overflowX = n.overflow[1], f.overflowY = n.overflow[2]
        }));
        for (i in t)
            if (o = t[i], Et.exec(o)) {
                if (delete t[i], s = s || "toggle" === o, o === (p ? "hide" : "show")) {
                    if ("show" !== o || !m || void 0 === m[i]) continue;
                    p = !0
                }
                h[i] = m && m[i] || fe.style(e, i)
            } else c = void 0;
        if (fe.isEmptyObject(h)) "inline" === ("none" === c ? A(e.nodeName) : c) && (f.display = c);
        else {
            m ? "hidden" in m && (p = m.hidden) : m = fe._data(e, "fxshow", {}), s && (m.hidden = !p), p ? fe(e).show() : d.done(function() {
                fe(e).hide()
            }), d.done(function() {
                var t;
                fe._removeData(e, "fxshow");
                for (t in h) fe.style(e, t, h[t])
            });
            for (i in h) r = P(p ? m[i] : 0, i, d), i in m || (m[i] = r.start, p && (r.end = r.start, r.start = "width" === i || "height" === i ? 1 : 0))
        }
    }

    function q(e, t) {
        var n, i, o, s, r;
        for (n in e)
            if (i = fe.camelCase(n), o = t[i], s = e[n], fe.isArray(s) && (o = s[1], s = e[n] = s[0]), n !== i && (e[i] = s, delete e[n]), r = fe.cssHooks[i], r && "expand" in r) {
                s = r.expand(s), delete e[i];
                for (n in s) n in e || (e[n] = s[n], t[n] = o)
            } else t[i] = o
    }

    function B(e, t, n) {
        var i, o, s = 0,
            r = B.prefilters.length,
            a = fe.Deferred().always(function() {
                delete l.elem
            }),
            l = function() {
                if (o) return !1;
                for (var t = kt || F(), n = Math.max(0, c.startTime + c.duration - t), i = n / c.duration || 0, s = 1 - i, r = 0, l = c.tweens.length; r < l; r++) c.tweens[r].run(s);
                return a.notifyWith(e, [c, s, n]), s < 1 && l ? n : (a.resolveWith(e, [c]), !1)
            },
            c = a.promise({
                elem: e,
                props: fe.extend({}, t),
                opts: fe.extend(!0, {
                    specialEasing: {},
                    easing: fe.easing._default
                }, n),
                originalProperties: t,
                originalOptions: n,
                startTime: kt || F(),
                duration: n.duration,
                tweens: [],
                createTween: function(t, n) {
                    var i = fe.Tween(e, c.opts, t, n, c.opts.specialEasing[t] || c.opts.easing);
                    return c.tweens.push(i), i
                },
                stop: function(t) {
                    var n = 0,
                        i = t ? c.tweens.length : 0;
                    if (o) return this;
                    for (o = !0; n < i; n++) c.tweens[n].run(1);
                    return t ? (a.notifyWith(e, [c, 1, 0]), a.resolveWith(e, [c, t])) : a.rejectWith(e, [c, t]), this
                }
            }),
            u = c.props;
        for (q(u, c.opts.specialEasing); s < r; s++)
            if (i = B.prefilters[s].call(c, e, u, c.opts)) return fe.isFunction(i.stop) && (fe._queueHooks(c.elem, c.opts.queue).stop = fe.proxy(i.stop, i)), i;
        return fe.map(u, P, c), fe.isFunction(c.opts.start) && c.opts.start.call(e, c), fe.fx.timer(fe.extend(l, {
            elem: e,
            anim: c,
            queue: c.opts.queue
        })), c.progress(c.opts.progress).done(c.opts.done, c.opts.complete).fail(c.opts.fail).always(c.opts.always)
    }

    function V(e) {
        return fe.attr(e, "class") || ""
    }

    function W(e) {
        return function(t, n) {
            "string" != typeof t && (n = t, t = "*");
            var i, o = 0,
                s = t.toLowerCase().match(Ne) || [];
            if (fe.isFunction(n))
                for (; i = s[o++];) "+" === i.charAt(0) ? (i = i.slice(1) || "*", (e[i] = e[i] || []).unshift(n)) : (e[i] = e[i] || []).push(n)
        }
    }

    function z(e, t, n, i) {
        function o(a) {
            var l;
            return s[a] = !0, fe.each(e[a] || [], function(e, a) {
                var c = a(t, n, i);
                return "string" != typeof c || r || s[c] ? r ? !(l = c) : void 0 : (t.dataTypes.unshift(c), o(c), !1)
            }), l
        }
        var s = {},
            r = e === Kt;
        return o(t.dataTypes[0]) || !s["*"] && o("*")
    }

    function Y(e, t) {
        var n, i, o = fe.ajaxSettings.flatOptions || {};
        for (i in t) void 0 !== t[i] && ((o[i] ? e : n || (n = {}))[i] = t[i]);
        return n && fe.extend(!0, e, n), e
    }

    function Q(e, t, n) {
        for (var i, o, s, r, a = e.contents, l = e.dataTypes;
            "*" === l[0];) l.shift(), void 0 === o && (o = e.mimeType || t.getResponseHeader("Content-Type"));
        if (o)
            for (r in a)
                if (a[r] && a[r].test(o)) {
                    l.unshift(r);
                    break
                }
        if (l[0] in n) s = l[0];
        else {
            for (r in n) {
                if (!l[0] || e.converters[r + " " + l[0]]) {
                    s = r;
                    break
                }
                i || (i = r)
            }
            s = s || i
        } if (s) return s !== l[0] && l.unshift(s), n[s]
    }

    function X(e, t, n, i) {
        var o, s, r, a, l, c = {},
            u = e.dataTypes.slice();
        if (u[1])
            for (r in e.converters) c[r.toLowerCase()] = e.converters[r];
        for (s = u.shift(); s;)
            if (e.responseFields[s] && (n[e.responseFields[s]] = t), !l && i && e.dataFilter && (t = e.dataFilter(t, e.dataType)), l = s, s = u.shift())
                if ("*" === s) s = l;
                else if ("*" !== l && l !== s) {
            if (r = c[l + " " + s] || c["* " + s], !r)
                for (o in c)
                    if (a = o.split(" "), a[1] === s && (r = c[l + " " + a[0]] || c["* " + a[0]])) {
                        r === !0 ? r = c[o] : c[o] !== !0 && (s = a[0], u.unshift(a[1]));
                        break
                    }
            if (r !== !0)
                if (r && e["throws"]) t = r(t);
                else try {
                    t = r(t)
                } catch (d) {
                    return {
                        state: "parsererror",
                        error: r ? d : "No conversion from " + l + " to " + s
                    }
                }
        }
        return {
            state: "success",
            data: t
        }
    }

    function J(e) {
        return e.style && e.style.display || fe.css(e, "display")
    }

    function G(e) {
        if (!fe.contains(e.ownerDocument || ie, e)) return !0;
        for (; e && 1 === e.nodeType;) {
            if ("none" === J(e) || "hidden" === e.type) return !0;
            e = e.parentNode
        }
        return !1
    }

    function K(e, t, n, i) {
        var o;
        if (fe.isArray(t)) fe.each(t, function(t, o) {
            n || on.test(e) ? i(e, o) : K(e + "[" + ("object" == typeof o && null != o ? t : "") + "]", o, n, i)
        });
        else if (n || "object" !== fe.type(t)) i(e, t);
        else
            for (o in t) K(e + "[" + o + "]", t[o], n, i)
    }

    function Z() {
        try {
            return new e.XMLHttpRequest
        } catch (t) {}
    }

    function ee() {
        try {
            return new e.ActiveXObject("Microsoft.XMLHTTP")
        } catch (t) {}
    }

    function te(e) {
        return fe.isWindow(e) ? e : 9 === e.nodeType && (e.defaultView || e.parentWindow)
    }
    var ne = [],
        ie = e.document,
        oe = ne.slice,
        se = ne.concat,
        re = ne.push,
        ae = ne.indexOf,
        le = {},
        ce = le.toString,
        ue = le.hasOwnProperty,
        de = {},
        he = "1.12.4",
        fe = function(e, t) {
            return new fe.fn.init(e, t)
        },
        pe = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g,
        me = /^-ms-/,
        ge = /-([\da-z])/gi,
        ve = function(e, t) {
            return t.toUpperCase()
        };
    fe.fn = fe.prototype = {
        jquery: he,
        constructor: fe,
        selector: "",
        length: 0,
        toArray: function() {
            return oe.call(this)
        },
        get: function(e) {
            return null != e ? e < 0 ? this[e + this.length] : this[e] : oe.call(this)
        },
        pushStack: function(e) {
            var t = fe.merge(this.constructor(), e);
            return t.prevObject = this, t.context = this.context, t
        },
        each: function(e) {
            return fe.each(this, e)
        },
        map: function(e) {
            return this.pushStack(fe.map(this, function(t, n) {
                return e.call(t, n, t)
            }))
        },
        slice: function() {
            return this.pushStack(oe.apply(this, arguments))
        },
        first: function() {
            return this.eq(0)
        },
        last: function() {
            return this.eq(-1)
        },
        eq: function(e) {
            var t = this.length,
                n = +e + (e < 0 ? t : 0);
            return this.pushStack(n >= 0 && n < t ? [this[n]] : [])
        },
        end: function() {
            return this.prevObject || this.constructor()
        },
        push: re,
        sort: ne.sort,
        splice: ne.splice
    }, fe.extend = fe.fn.extend = function() {
        var e, t, n, i, o, s, r = arguments[0] || {},
            a = 1,
            l = arguments.length,
            c = !1;
        for ("boolean" == typeof r && (c = r, r = arguments[a] || {}, a++), "object" == typeof r || fe.isFunction(r) || (r = {}), a === l && (r = this, a--); a < l; a++)
            if (null != (o = arguments[a]))
                for (i in o) e = r[i], n = o[i], r !== n && (c && n && (fe.isPlainObject(n) || (t = fe.isArray(n))) ? (t ? (t = !1, s = e && fe.isArray(e) ? e : []) : s = e && fe.isPlainObject(e) ? e : {}, r[i] = fe.extend(c, s, n)) : void 0 !== n && (r[i] = n));
        return r
    }, fe.extend({
        expando: "jQuery" + (he + Math.random()).replace(/\D/g, ""),
        isReady: !0,
        error: function(e) {
            throw new Error(e)
        },
        noop: function() {},
        isFunction: function(e) {
            return "function" === fe.type(e)
        },
        isArray: Array.isArray || function(e) {
            return "array" === fe.type(e)
        },
        isWindow: function(e) {
            return null != e && e == e.window
        },
        isNumeric: function(e) {
            var t = e && e.toString();
            return !fe.isArray(e) && t - parseFloat(t) + 1 >= 0
        },
        isEmptyObject: function(e) {
            var t;
            for (t in e) return !1;
            return !0
        },
        isPlainObject: function(e) {
            var t;
            if (!e || "object" !== fe.type(e) || e.nodeType || fe.isWindow(e)) return !1;
            try {
                if (e.constructor && !ue.call(e, "constructor") && !ue.call(e.constructor.prototype, "isPrototypeOf")) return !1
            } catch (n) {
                return !1
            }
            if (!de.ownFirst)
                for (t in e) return ue.call(e, t);
            for (t in e);
            return void 0 === t || ue.call(e, t)
        },
        type: function(e) {
            return null == e ? e + "" : "object" == typeof e || "function" == typeof e ? le[ce.call(e)] || "object" : typeof e
        },
        globalEval: function(t) {
            t && fe.trim(t) && (e.execScript || function(t) {
                e.eval.call(e, t)
            })(t)
        },
        camelCase: function(e) {
            return e.replace(me, "ms-").replace(ge, ve)
        },
        nodeName: function(e, t) {
            return e.nodeName && e.nodeName.toLowerCase() === t.toLowerCase()
        },
        each: function(e, t) {
            var i, o = 0;
            if (n(e))
                for (i = e.length; o < i && t.call(e[o], o, e[o]) !== !1; o++);
            else
                for (o in e)
                    if (t.call(e[o], o, e[o]) === !1) break; return e
        },
        trim: function(e) {
            return null == e ? "" : (e + "").replace(pe, "")
        },
        makeArray: function(e, t) {
            var i = t || [];
            return null != e && (n(Object(e)) ? fe.merge(i, "string" == typeof e ? [e] : e) : re.call(i, e)), i
        },
        inArray: function(e, t, n) {
            var i;
            if (t) {
                if (ae) return ae.call(t, e, n);
                for (i = t.length, n = n ? n < 0 ? Math.max(0, i + n) : n : 0; n < i; n++)
                    if (n in t && t[n] === e) return n
            }
            return -1
        },
        merge: function(e, t) {
            for (var n = +t.length, i = 0, o = e.length; i < n;) e[o++] = t[i++];
            if (n !== n)
                for (; void 0 !== t[i];) e[o++] = t[i++];
            return e.length = o, e
        },
        grep: function(e, t, n) {
            for (var i, o = [], s = 0, r = e.length, a = !n; s < r; s++) i = !t(e[s], s), i !== a && o.push(e[s]);
            return o
        },
        map: function(e, t, i) {
            var o, s, r = 0,
                a = [];
            if (n(e))
                for (o = e.length; r < o; r++) s = t(e[r], r, i), null != s && a.push(s);
            else
                for (r in e) s = t(e[r], r, i), null != s && a.push(s);
            return se.apply([], a)
        },
        guid: 1,
        proxy: function(e, t) {
            var n, i, o;
            if ("string" == typeof t && (o = e[t], t = e, e = o), fe.isFunction(e)) return n = oe.call(arguments, 2), i = function() {
                return e.apply(t || this, n.concat(oe.call(arguments)))
            }, i.guid = e.guid = e.guid || fe.guid++, i
        },
        now: function() {
            return +new Date
        },
        support: de
    }), "function" == typeof Symbol && (fe.fn[Symbol.iterator] = ne[Symbol.iterator]), fe.each("Boolean Number String Function Array Date RegExp Object Error Symbol".split(" "), function(e, t) {
        le["[object " + t + "]"] = t.toLowerCase()
    });
    var ye = function(e) {
        function t(e, t, n, i) {
            var o, s, r, a, l, c, d, f, p = t && t.ownerDocument,
                m = t ? t.nodeType : 9;
            if (n = n || [], "string" != typeof e || !e || 1 !== m && 9 !== m && 11 !== m) return n;
            if (!i && ((t ? t.ownerDocument || t : P) !== O && I(t), t = t || O, U)) {
                if (11 !== m && (c = ve.exec(e)))
                    if (o = c[1]) {
                        if (9 === m) {
                            if (!(r = t.getElementById(o))) return n;
                            if (r.id === o) return n.push(r), n
                        } else if (p && (r = p.getElementById(o)) && F(t, r) && r.id === o) return n.push(r), n
                    } else {
                        if (c[2]) return K.apply(n, t.getElementsByTagName(e)), n;
                        if ((o = c[3]) && T.getElementsByClassName && t.getElementsByClassName) return K.apply(n, t.getElementsByClassName(o)), n
                    }
                if (T.qsa && !W[e + " "] && (!j || !j.test(e))) {
                    if (1 !== m) p = t, f = e;
                    else if ("object" !== t.nodeName.toLowerCase()) {
                        for ((a = t.getAttribute("id")) ? a = a.replace(be, "\\$&") : t.setAttribute("id", a = R), d = k(e), s = d.length, l = he.test(a) ? "#" + a : "[id='" + a + "']"; s--;) d[s] = l + " " + h(d[s]);
                        f = d.join(","), p = ye.test(e) && u(t.parentNode) || t
                    }
                    if (f) try {
                        return K.apply(n, p.querySelectorAll(f)), n
                    } catch (g) {} finally {
                        a === R && t.removeAttribute("id")
                    }
                }
            }
            return E(e.replace(ae, "$1"), t, n, i)
        }

        function n() {
            function e(n, i) {
                return t.push(n + " ") > x.cacheLength && delete e[t.shift()], e[n + " "] = i
            }
            var t = [];
            return e
        }

        function i(e) {
            return e[R] = !0, e
        }

        function o(e) {
            var t = O.createElement("div");
            try {
                return !!e(t)
            } catch (n) {
                return !1
            } finally {
                t.parentNode && t.parentNode.removeChild(t), t = null
            }
        }

        function s(e, t) {
            for (var n = e.split("|"), i = n.length; i--;) x.attrHandle[n[i]] = t
        }

        function r(e, t) {
            var n = t && e,
                i = n && 1 === e.nodeType && 1 === t.nodeType && (~t.sourceIndex || Y) - (~e.sourceIndex || Y);
            if (i) return i;
            if (n)
                for (; n = n.nextSibling;)
                    if (n === t) return -1;
            return e ? 1 : -1
        }

        function a(e) {
            return function(t) {
                var n = t.nodeName.toLowerCase();
                return "input" === n && t.type === e
            }
        }

        function l(e) {
            return function(t) {
                var n = t.nodeName.toLowerCase();
                return ("input" === n || "button" === n) && t.type === e
            }
        }

        function c(e) {
            return i(function(t) {
                return t = +t, i(function(n, i) {
                    for (var o, s = e([], n.length, t), r = s.length; r--;) n[o = s[r]] && (n[o] = !(i[o] = n[o]))
                })
            })
        }

        function u(e) {
            return e && "undefined" != typeof e.getElementsByTagName && e
        }

        function d() {}

        function h(e) {
            for (var t = 0, n = e.length, i = ""; t < n; t++) i += e[t].value;
            return i
        }

        function f(e, t, n) {
            var i = t.dir,
                o = n && "parentNode" === i,
                s = q++;
            return t.first ? function(t, n, s) {
                for (; t = t[i];)
                    if (1 === t.nodeType || o) return e(t, n, s)
            } : function(t, n, r) {
                var a, l, c, u = [_, s];
                if (r) {
                    for (; t = t[i];)
                        if ((1 === t.nodeType || o) && e(t, n, r)) return !0
                } else
                    for (; t = t[i];)
                        if (1 === t.nodeType || o) {
                            if (c = t[R] || (t[R] = {}), l = c[t.uniqueID] || (c[t.uniqueID] = {}), (a = l[i]) && a[0] === _ && a[1] === s) return u[2] = a[2];
                            if (l[i] = u, u[2] = e(t, n, r)) return !0
                        }
            }
        }

        function p(e) {
            return e.length > 1 ? function(t, n, i) {
                for (var o = e.length; o--;)
                    if (!e[o](t, n, i)) return !1;
                return !0
            } : e[0]
        }

        function m(e, n, i) {
            for (var o = 0, s = n.length; o < s; o++) t(e, n[o], i);
            return i
        }

        function g(e, t, n, i, o) {
            for (var s, r = [], a = 0, l = e.length, c = null != t; a < l; a++)(s = e[a]) && (n && !n(s, i, o) || (r.push(s), c && t.push(a)));
            return r
        }

        function v(e, t, n, o, s, r) {
            return o && !o[R] && (o = v(o)), s && !s[R] && (s = v(s, r)), i(function(i, r, a, l) {
                var c, u, d, h = [],
                    f = [],
                    p = r.length,
                    v = i || m(t || "*", a.nodeType ? [a] : a, []),
                    y = !e || !i && t ? v : g(v, h, e, a, l),
                    b = n ? s || (i ? e : p || o) ? [] : r : y;
                if (n && n(y, b, a, l), o)
                    for (c = g(b, f), o(c, [], a, l), u = c.length; u--;)(d = c[u]) && (b[f[u]] = !(y[f[u]] = d));
                if (i) {
                    if (s || e) {
                        if (s) {
                            for (c = [], u = b.length; u--;)(d = b[u]) && c.push(y[u] = d);
                            s(null, b = [], c, l)
                        }
                        for (u = b.length; u--;)(d = b[u]) && (c = s ? ee(i, d) : h[u]) > -1 && (i[c] = !(r[c] = d))
                    }
                } else b = g(b === r ? b.splice(p, b.length) : b), s ? s(null, r, b, l) : K.apply(r, b)
            })
        }

        function y(e) {
            for (var t, n, i, o = e.length, s = x.relative[e[0].type], r = s || x.relative[" "], a = s ? 1 : 0, l = f(function(e) {
                return e === t
            }, r, !0), c = f(function(e) {
                return ee(t, e) > -1
            }, r, !0), u = [
                function(e, n, i) {
                    var o = !s && (i || n !== M) || ((t = n).nodeType ? l(e, n, i) : c(e, n, i));
                    return t = null, o
                }
            ]; a < o; a++)
                if (n = x.relative[e[a].type]) u = [f(p(u), n)];
                else {
                    if (n = x.filter[e[a].type].apply(null, e[a].matches), n[R]) {
                        for (i = ++a; i < o && !x.relative[e[i].type]; i++);
                        return v(a > 1 && p(u), a > 1 && h(e.slice(0, a - 1).concat({
                            value: " " === e[a - 2].type ? "*" : ""
                        })).replace(ae, "$1"), n, a < i && y(e.slice(a, i)), i < o && y(e = e.slice(i)), i < o && h(e))
                    }
                    u.push(n)
                }
            return p(u)
        }

        function b(e, n) {
            var o = n.length > 0,
                s = e.length > 0,
                r = function(i, r, a, l, c) {
                    var u, d, h, f = 0,
                        p = "0",
                        m = i && [],
                        v = [],
                        y = M,
                        b = i || s && x.find.TAG("*", c),
                        w = _ += null == y ? 1 : Math.random() || .1,
                        T = b.length;
                    for (c && (M = r === O || r || c); p !== T && null != (u = b[p]); p++) {
                        if (s && u) {
                            for (d = 0, r || u.ownerDocument === O || (I(u), a = !U); h = e[d++];)
                                if (h(u, r || O, a)) {
                                    l.push(u);
                                    break
                                }
                            c && (_ = w)
                        }
                        o && ((u = !h && u) && f--, i && m.push(u))
                    }
                    if (f += p, o && p !== f) {
                        for (d = 0; h = n[d++];) h(m, v, r, a);
                        if (i) {
                            if (f > 0)
                                for (; p--;) m[p] || v[p] || (v[p] = J.call(l));
                            v = g(v)
                        }
                        K.apply(l, v), c && !i && v.length > 0 && f + n.length > 1 && t.uniqueSort(l)
                    }
                    return c && (_ = w, M = y), m
                };
            return o ? i(r) : r
        }
        var w, T, x, C, D, k, S, E, M, N, A, I, O, $, U, j, L, H, F, R = "sizzle" + 1 * new Date,
            P = e.document,
            _ = 0,
            q = 0,
            B = n(),
            V = n(),
            W = n(),
            z = function(e, t) {
                return e === t && (A = !0), 0
            },
            Y = 1 << 31,
            Q = {}.hasOwnProperty,
            X = [],
            J = X.pop,
            G = X.push,
            K = X.push,
            Z = X.slice,
            ee = function(e, t) {
                for (var n = 0, i = e.length; n < i; n++)
                    if (e[n] === t) return n;
                return -1
            },
            te = "checked|selected|async|autofocus|autoplay|controls|defer|disabled|hidden|ismap|loop|multiple|open|readonly|required|scoped",
            ne = "[\\x20\\t\\r\\n\\f]",
            ie = "(?:\\\\.|[\\w-]|[^\\x00-\\xa0])+",
            oe = "\\[" + ne + "*(" + ie + ")(?:" + ne + "*([*^$|!~]?=)" + ne + "*(?:'((?:\\\\.|[^\\\\'])*)'|\"((?:\\\\.|[^\\\\\"])*)\"|(" + ie + "))|)" + ne + "*\\]",
            se = ":(" + ie + ")(?:\\((('((?:\\\\.|[^\\\\'])*)'|\"((?:\\\\.|[^\\\\\"])*)\")|((?:\\\\.|[^\\\\()[\\]]|" + oe + ")*)|.*)\\)|)",
            re = new RegExp(ne + "+", "g"),
            ae = new RegExp("^" + ne + "+|((?:^|[^\\\\])(?:\\\\.)*)" + ne + "+$", "g"),
            le = new RegExp("^" + ne + "*," + ne + "*"),
            ce = new RegExp("^" + ne + "*([>+~]|" + ne + ")" + ne + "*"),
            ue = new RegExp("=" + ne + "*([^\\]'\"]*?)" + ne + "*\\]", "g"),
            de = new RegExp(se),
            he = new RegExp("^" + ie + "$"),
            fe = {
                ID: new RegExp("^#(" + ie + ")"),
                CLASS: new RegExp("^\\.(" + ie + ")"),
                TAG: new RegExp("^(" + ie + "|[*])"),
                ATTR: new RegExp("^" + oe),
                PSEUDO: new RegExp("^" + se),
                CHILD: new RegExp("^:(only|first|last|nth|nth-last)-(child|of-type)(?:\\(" + ne + "*(even|odd|(([+-]|)(\\d*)n|)" + ne + "*(?:([+-]|)" + ne + "*(\\d+)|))" + ne + "*\\)|)", "i"),
                bool: new RegExp("^(?:" + te + ")$", "i"),
                needsContext: new RegExp("^" + ne + "*[>+~]|:(even|odd|eq|gt|lt|nth|first|last)(?:\\(" + ne + "*((?:-\\d)?\\d*)" + ne + "*\\)|)(?=[^-]|$)", "i")
            },
            pe = /^(?:input|select|textarea|button)$/i,
            me = /^h\d$/i,
            ge = /^[^{]+\{\s*\[native \w/,
            ve = /^(?:#([\w-]+)|(\w+)|\.([\w-]+))$/,
            ye = /[+~]/,
            be = /'|\\/g,
            we = new RegExp("\\\\([\\da-f]{1,6}" + ne + "?|(" + ne + ")|.)", "ig"),
            Te = function(e, t, n) {
                var i = "0x" + t - 65536;
                return i !== i || n ? t : i < 0 ? String.fromCharCode(i + 65536) : String.fromCharCode(i >> 10 | 55296, 1023 & i | 56320)
            },
            xe = function() {
                I()
            };
        try {
            K.apply(X = Z.call(P.childNodes), P.childNodes), X[P.childNodes.length].nodeType
        } catch (Ce) {
            K = {
                apply: X.length ? function(e, t) {
                    G.apply(e, Z.call(t))
                } : function(e, t) {
                    for (var n = e.length, i = 0; e[n++] = t[i++];);
                    e.length = n - 1
                }
            }
        }
        T = t.support = {}, D = t.isXML = function(e) {
            var t = e && (e.ownerDocument || e).documentElement;
            return !!t && "HTML" !== t.nodeName
        }, I = t.setDocument = function(e) {
            var t, n, i = e ? e.ownerDocument || e : P;
            return i !== O && 9 === i.nodeType && i.documentElement ? (O = i, $ = O.documentElement, U = !D(O), (n = O.defaultView) && n.top !== n && (n.addEventListener ? n.addEventListener("unload", xe, !1) : n.attachEvent && n.attachEvent("onunload", xe)), T.attributes = o(function(e) {
                return e.className = "i", !e.getAttribute("className")
            }), T.getElementsByTagName = o(function(e) {
                return e.appendChild(O.createComment("")), !e.getElementsByTagName("*").length
            }), T.getElementsByClassName = ge.test(O.getElementsByClassName), T.getById = o(function(e) {
                return $.appendChild(e).id = R, !O.getElementsByName || !O.getElementsByName(R).length
            }), T.getById ? (x.find.ID = function(e, t) {
                if ("undefined" != typeof t.getElementById && U) {
                    var n = t.getElementById(e);
                    return n ? [n] : []
                }
            }, x.filter.ID = function(e) {
                var t = e.replace(we, Te);
                return function(e) {
                    return e.getAttribute("id") === t
                }
            }) : (delete x.find.ID, x.filter.ID = function(e) {
                var t = e.replace(we, Te);
                return function(e) {
                    var n = "undefined" != typeof e.getAttributeNode && e.getAttributeNode("id");
                    return n && n.value === t
                }
            }), x.find.TAG = T.getElementsByTagName ? function(e, t) {
                return "undefined" != typeof t.getElementsByTagName ? t.getElementsByTagName(e) : T.qsa ? t.querySelectorAll(e) : void 0
            } : function(e, t) {
                var n, i = [],
                    o = 0,
                    s = t.getElementsByTagName(e);
                if ("*" === e) {
                    for (; n = s[o++];) 1 === n.nodeType && i.push(n);
                    return i
                }
                return s
            }, x.find.CLASS = T.getElementsByClassName && function(e, t) {
                if ("undefined" != typeof t.getElementsByClassName && U) return t.getElementsByClassName(e)
            }, L = [], j = [], (T.qsa = ge.test(O.querySelectorAll)) && (o(function(e) {
                $.appendChild(e).innerHTML = "<a id='" + R + "'></a><select id='" + R + "-\r\\' msallowcapture=''><option selected=''></option></select>", e.querySelectorAll("[msallowcapture^='']").length && j.push("[*^$]=" + ne + "*(?:''|\"\")"), e.querySelectorAll("[selected]").length || j.push("\\[" + ne + "*(?:value|" + te + ")"), e.querySelectorAll("[id~=" + R + "-]").length || j.push("~="), e.querySelectorAll(":checked").length || j.push(":checked"), e.querySelectorAll("a#" + R + "+*").length || j.push(".#.+[+~]")
            }), o(function(e) {
                var t = O.createElement("input");
                t.setAttribute("type", "hidden"), e.appendChild(t).setAttribute("name", "D"), e.querySelectorAll("[name=d]").length && j.push("name" + ne + "*[*^$|!~]?="), e.querySelectorAll(":enabled").length || j.push(":enabled", ":disabled"), e.querySelectorAll("*,:x"), j.push(",.*:")
            })), (T.matchesSelector = ge.test(H = $.matches || $.webkitMatchesSelector || $.mozMatchesSelector || $.oMatchesSelector || $.msMatchesSelector)) && o(function(e) {
                T.disconnectedMatch = H.call(e, "div"), H.call(e, "[s!='']:x"), L.push("!=", se)
            }), j = j.length && new RegExp(j.join("|")), L = L.length && new RegExp(L.join("|")), t = ge.test($.compareDocumentPosition), F = t || ge.test($.contains) ? function(e, t) {
                var n = 9 === e.nodeType ? e.documentElement : e,
                    i = t && t.parentNode;
                return e === i || !(!i || 1 !== i.nodeType || !(n.contains ? n.contains(i) : e.compareDocumentPosition && 16 & e.compareDocumentPosition(i)))
            } : function(e, t) {
                if (t)
                    for (; t = t.parentNode;)
                        if (t === e) return !0;
                return !1
            }, z = t ? function(e, t) {
                if (e === t) return A = !0, 0;
                var n = !e.compareDocumentPosition - !t.compareDocumentPosition;
                return n ? n : (n = (e.ownerDocument || e) === (t.ownerDocument || t) ? e.compareDocumentPosition(t) : 1, 1 & n || !T.sortDetached && t.compareDocumentPosition(e) === n ? e === O || e.ownerDocument === P && F(P, e) ? -1 : t === O || t.ownerDocument === P && F(P, t) ? 1 : N ? ee(N, e) - ee(N, t) : 0 : 4 & n ? -1 : 1)
            } : function(e, t) {
                if (e === t) return A = !0, 0;
                var n, i = 0,
                    o = e.parentNode,
                    s = t.parentNode,
                    a = [e],
                    l = [t];
                if (!o || !s) return e === O ? -1 : t === O ? 1 : o ? -1 : s ? 1 : N ? ee(N, e) - ee(N, t) : 0;
                if (o === s) return r(e, t);
                for (n = e; n = n.parentNode;) a.unshift(n);
                for (n = t; n = n.parentNode;) l.unshift(n);
                for (; a[i] === l[i];) i++;
                return i ? r(a[i], l[i]) : a[i] === P ? -1 : l[i] === P ? 1 : 0
            }, O) : O
        }, t.matches = function(e, n) {
            return t(e, null, null, n)
        }, t.matchesSelector = function(e, n) {
            if ((e.ownerDocument || e) !== O && I(e), n = n.replace(ue, "='$1']"), T.matchesSelector && U && !W[n + " "] && (!L || !L.test(n)) && (!j || !j.test(n))) try {
                var i = H.call(e, n);
                if (i || T.disconnectedMatch || e.document && 11 !== e.document.nodeType) return i
            } catch (o) {}
            return t(n, O, null, [e]).length > 0
        }, t.contains = function(e, t) {
            return (e.ownerDocument || e) !== O && I(e), F(e, t)
        }, t.attr = function(e, t) {
            (e.ownerDocument || e) !== O && I(e);
            var n = x.attrHandle[t.toLowerCase()],
                i = n && Q.call(x.attrHandle, t.toLowerCase()) ? n(e, t, !U) : void 0;
            return void 0 !== i ? i : T.attributes || !U ? e.getAttribute(t) : (i = e.getAttributeNode(t)) && i.specified ? i.value : null
        }, t.error = function(e) {
            throw new Error("Syntax error, unrecognized expression: " + e)
        }, t.uniqueSort = function(e) {
            var t, n = [],
                i = 0,
                o = 0;
            if (A = !T.detectDuplicates, N = !T.sortStable && e.slice(0), e.sort(z), A) {
                for (; t = e[o++];) t === e[o] && (i = n.push(o));
                for (; i--;) e.splice(n[i], 1)
            }
            return N = null, e
        }, C = t.getText = function(e) {
            var t, n = "",
                i = 0,
                o = e.nodeType;
            if (o) {
                if (1 === o || 9 === o || 11 === o) {
                    if ("string" == typeof e.textContent) return e.textContent;
                    for (e = e.firstChild; e; e = e.nextSibling) n += C(e)
                } else if (3 === o || 4 === o) return e.nodeValue
            } else
                for (; t = e[i++];) n += C(t);
            return n
        }, x = t.selectors = {
            cacheLength: 50,
            createPseudo: i,
            match: fe,
            attrHandle: {},
            find: {},
            relative: {
                ">": {
                    dir: "parentNode",
                    first: !0
                },
                " ": {
                    dir: "parentNode"
                },
                "+": {
                    dir: "previousSibling",
                    first: !0
                },
                "~": {
                    dir: "previousSibling"
                }
            },
            preFilter: {
                ATTR: function(e) {
                    return e[1] = e[1].replace(we, Te), e[3] = (e[3] || e[4] || e[5] || "").replace(we, Te), "~=" === e[2] && (e[3] = " " + e[3] + " "), e.slice(0, 4)
                },
                CHILD: function(e) {
                    return e[1] = e[1].toLowerCase(), "nth" === e[1].slice(0, 3) ? (e[3] || t.error(e[0]), e[4] = +(e[4] ? e[5] + (e[6] || 1) : 2 * ("even" === e[3] || "odd" === e[3])), e[5] = +(e[7] + e[8] || "odd" === e[3])) : e[3] && t.error(e[0]), e
                },
                PSEUDO: function(e) {
                    var t, n = !e[6] && e[2];
                    return fe.CHILD.test(e[0]) ? null : (e[3] ? e[2] = e[4] || e[5] || "" : n && de.test(n) && (t = k(n, !0)) && (t = n.indexOf(")", n.length - t) - n.length) && (e[0] = e[0].slice(0, t), e[2] = n.slice(0, t)), e.slice(0, 3))
                }
            },
            filter: {
                TAG: function(e) {
                    var t = e.replace(we, Te).toLowerCase();
                    return "*" === e ? function() {
                        return !0
                    } : function(e) {
                        return e.nodeName && e.nodeName.toLowerCase() === t
                    }
                },
                CLASS: function(e) {
                    var t = B[e + " "];
                    return t || (t = new RegExp("(^|" + ne + ")" + e + "(" + ne + "|$)")) && B(e, function(e) {
                        return t.test("string" == typeof e.className && e.className || "undefined" != typeof e.getAttribute && e.getAttribute("class") || "")
                    })
                },
                ATTR: function(e, n, i) {
                    return function(o) {
                        var s = t.attr(o, e);
                        return null == s ? "!=" === n : !n || (s += "", "=" === n ? s === i : "!=" === n ? s !== i : "^=" === n ? i && 0 === s.indexOf(i) : "*=" === n ? i && s.indexOf(i) > -1 : "$=" === n ? i && s.slice(-i.length) === i : "~=" === n ? (" " + s.replace(re, " ") + " ").indexOf(i) > -1 : "|=" === n && (s === i || s.slice(0, i.length + 1) === i + "-"))
                    }
                },
                CHILD: function(e, t, n, i, o) {
                    var s = "nth" !== e.slice(0, 3),
                        r = "last" !== e.slice(-4),
                        a = "of-type" === t;
                    return 1 === i && 0 === o ? function(e) {
                        return !!e.parentNode
                    } : function(t, n, l) {
                        var c, u, d, h, f, p, m = s !== r ? "nextSibling" : "previousSibling",
                            g = t.parentNode,
                            v = a && t.nodeName.toLowerCase(),
                            y = !l && !a,
                            b = !1;
                        if (g) {
                            if (s) {
                                for (; m;) {
                                    for (h = t; h = h[m];)
                                        if (a ? h.nodeName.toLowerCase() === v : 1 === h.nodeType) return !1;
                                    p = m = "only" === e && !p && "nextSibling"
                                }
                                return !0
                            }
                            if (p = [r ? g.firstChild : g.lastChild], r && y) {
                                for (h = g, d = h[R] || (h[R] = {}), u = d[h.uniqueID] || (d[h.uniqueID] = {}), c = u[e] || [], f = c[0] === _ && c[1], b = f && c[2],
                                    h = f && g.childNodes[f]; h = ++f && h && h[m] || (b = f = 0) || p.pop();)
                                    if (1 === h.nodeType && ++b && h === t) {
                                        u[e] = [_, f, b];
                                        break
                                    }
                            } else if (y && (h = t, d = h[R] || (h[R] = {}), u = d[h.uniqueID] || (d[h.uniqueID] = {}), c = u[e] || [], f = c[0] === _ && c[1], b = f), b === !1)
                                for (;
                                    (h = ++f && h && h[m] || (b = f = 0) || p.pop()) && ((a ? h.nodeName.toLowerCase() !== v : 1 !== h.nodeType) || !++b || (y && (d = h[R] || (h[R] = {}), u = d[h.uniqueID] || (d[h.uniqueID] = {}), u[e] = [_, b]), h !== t)););
                            return b -= o, b === i || b % i === 0 && b / i >= 0
                        }
                    }
                },
                PSEUDO: function(e, n) {
                    var o, s = x.pseudos[e] || x.setFilters[e.toLowerCase()] || t.error("unsupported pseudo: " + e);
                    return s[R] ? s(n) : s.length > 1 ? (o = [e, e, "", n], x.setFilters.hasOwnProperty(e.toLowerCase()) ? i(function(e, t) {
                        for (var i, o = s(e, n), r = o.length; r--;) i = ee(e, o[r]), e[i] = !(t[i] = o[r])
                    }) : function(e) {
                        return s(e, 0, o)
                    }) : s
                }
            },
            pseudos: {
                not: i(function(e) {
                    var t = [],
                        n = [],
                        o = S(e.replace(ae, "$1"));
                    return o[R] ? i(function(e, t, n, i) {
                        for (var s, r = o(e, null, i, []), a = e.length; a--;)(s = r[a]) && (e[a] = !(t[a] = s))
                    }) : function(e, i, s) {
                        return t[0] = e, o(t, null, s, n), t[0] = null, !n.pop()
                    }
                }),
                has: i(function(e) {
                    return function(n) {
                        return t(e, n).length > 0
                    }
                }),
                contains: i(function(e) {
                    return e = e.replace(we, Te),
                        function(t) {
                            return (t.textContent || t.innerText || C(t)).indexOf(e) > -1
                        }
                }),
                lang: i(function(e) {
                    return he.test(e || "") || t.error("unsupported lang: " + e), e = e.replace(we, Te).toLowerCase(),
                        function(t) {
                            var n;
                            do
                                if (n = U ? t.lang : t.getAttribute("xml:lang") || t.getAttribute("lang")) return n = n.toLowerCase(), n === e || 0 === n.indexOf(e + "-");
                            while ((t = t.parentNode) && 1 === t.nodeType);
                            return !1
                        }
                }),
                target: function(t) {
                    var n = e.location && e.location.hash;
                    return n && n.slice(1) === t.id
                },
                root: function(e) {
                    return e === $
                },
                focus: function(e) {
                    return e === O.activeElement && (!O.hasFocus || O.hasFocus()) && !!(e.type || e.href || ~e.tabIndex)
                },
                enabled: function(e) {
                    return e.disabled === !1
                },
                disabled: function(e) {
                    return e.disabled === !0
                },
                checked: function(e) {
                    var t = e.nodeName.toLowerCase();
                    return "input" === t && !!e.checked || "option" === t && !!e.selected
                },
                selected: function(e) {
                    return e.parentNode && e.parentNode.selectedIndex, e.selected === !0
                },
                empty: function(e) {
                    for (e = e.firstChild; e; e = e.nextSibling)
                        if (e.nodeType < 6) return !1;
                    return !0
                },
                parent: function(e) {
                    return !x.pseudos.empty(e)
                },
                header: function(e) {
                    return me.test(e.nodeName)
                },
                input: function(e) {
                    return pe.test(e.nodeName)
                },
                button: function(e) {
                    var t = e.nodeName.toLowerCase();
                    return "input" === t && "button" === e.type || "button" === t
                },
                text: function(e) {
                    var t;
                    return "input" === e.nodeName.toLowerCase() && "text" === e.type && (null == (t = e.getAttribute("type")) || "text" === t.toLowerCase())
                },
                first: c(function() {
                    return [0]
                }),
                last: c(function(e, t) {
                    return [t - 1]
                }),
                eq: c(function(e, t, n) {
                    return [n < 0 ? n + t : n]
                }),
                even: c(function(e, t) {
                    for (var n = 0; n < t; n += 2) e.push(n);
                    return e
                }),
                odd: c(function(e, t) {
                    for (var n = 1; n < t; n += 2) e.push(n);
                    return e
                }),
                lt: c(function(e, t, n) {
                    for (var i = n < 0 ? n + t : n; --i >= 0;) e.push(i);
                    return e
                }),
                gt: c(function(e, t, n) {
                    for (var i = n < 0 ? n + t : n; ++i < t;) e.push(i);
                    return e
                })
            }
        }, x.pseudos.nth = x.pseudos.eq;
        for (w in {
            radio: !0,
            checkbox: !0,
            file: !0,
            password: !0,
            image: !0
        }) x.pseudos[w] = a(w);
        for (w in {
            submit: !0,
            reset: !0
        }) x.pseudos[w] = l(w);
        return d.prototype = x.filters = x.pseudos, x.setFilters = new d, k = t.tokenize = function(e, n) {
            var i, o, s, r, a, l, c, u = V[e + " "];
            if (u) return n ? 0 : u.slice(0);
            for (a = e, l = [], c = x.preFilter; a;) {
                i && !(o = le.exec(a)) || (o && (a = a.slice(o[0].length) || a), l.push(s = [])), i = !1, (o = ce.exec(a)) && (i = o.shift(), s.push({
                    value: i,
                    type: o[0].replace(ae, " ")
                }), a = a.slice(i.length));
                for (r in x.filter)!(o = fe[r].exec(a)) || c[r] && !(o = c[r](o)) || (i = o.shift(), s.push({
                    value: i,
                    type: r,
                    matches: o
                }), a = a.slice(i.length));
                if (!i) break
            }
            return n ? a.length : a ? t.error(e) : V(e, l).slice(0)
        }, S = t.compile = function(e, t) {
            var n, i = [],
                o = [],
                s = W[e + " "];
            if (!s) {
                for (t || (t = k(e)), n = t.length; n--;) s = y(t[n]), s[R] ? i.push(s) : o.push(s);
                s = W(e, b(o, i)), s.selector = e
            }
            return s
        }, E = t.select = function(e, t, n, i) {
            var o, s, r, a, l, c = "function" == typeof e && e,
                d = !i && k(e = c.selector || e);
            if (n = n || [], 1 === d.length) {
                if (s = d[0] = d[0].slice(0), s.length > 2 && "ID" === (r = s[0]).type && T.getById && 9 === t.nodeType && U && x.relative[s[1].type]) {
                    if (t = (x.find.ID(r.matches[0].replace(we, Te), t) || [])[0], !t) return n;
                    c && (t = t.parentNode), e = e.slice(s.shift().value.length)
                }
                for (o = fe.needsContext.test(e) ? 0 : s.length; o-- && (r = s[o], !x.relative[a = r.type]);)
                    if ((l = x.find[a]) && (i = l(r.matches[0].replace(we, Te), ye.test(s[0].type) && u(t.parentNode) || t))) {
                        if (s.splice(o, 1), e = i.length && h(s), !e) return K.apply(n, i), n;
                        break
                    }
            }
            return (c || S(e, d))(i, t, !U, n, !t || ye.test(e) && u(t.parentNode) || t), n
        }, T.sortStable = R.split("").sort(z).join("") === R, T.detectDuplicates = !!A, I(), T.sortDetached = o(function(e) {
            return 1 & e.compareDocumentPosition(O.createElement("div"))
        }), o(function(e) {
            return e.innerHTML = "<a href='#'></a>", "#" === e.firstChild.getAttribute("href")
        }) || s("type|href|height|width", function(e, t, n) {
            if (!n) return e.getAttribute(t, "type" === t.toLowerCase() ? 1 : 2)
        }), T.attributes && o(function(e) {
            return e.innerHTML = "<input/>", e.firstChild.setAttribute("value", ""), "" === e.firstChild.getAttribute("value")
        }) || s("value", function(e, t, n) {
            if (!n && "input" === e.nodeName.toLowerCase()) return e.defaultValue
        }), o(function(e) {
            return null == e.getAttribute("disabled")
        }) || s(te, function(e, t, n) {
            var i;
            if (!n) return e[t] === !0 ? t.toLowerCase() : (i = e.getAttributeNode(t)) && i.specified ? i.value : null
        }), t
    }(e);
    fe.find = ye, fe.expr = ye.selectors, fe.expr[":"] = fe.expr.pseudos, fe.uniqueSort = fe.unique = ye.uniqueSort, fe.text = ye.getText, fe.isXMLDoc = ye.isXML, fe.contains = ye.contains;
    var be = function(e, t, n) {
            for (var i = [], o = void 0 !== n;
                (e = e[t]) && 9 !== e.nodeType;)
                if (1 === e.nodeType) {
                    if (o && fe(e).is(n)) break;
                    i.push(e)
                }
            return i
        },
        we = function(e, t) {
            for (var n = []; e; e = e.nextSibling) 1 === e.nodeType && e !== t && n.push(e);
            return n
        },
        Te = fe.expr.match.needsContext,
        xe = /^<([\w-]+)\s*\/?>(?:<\/\1>|)$/,
        Ce = /^.[^:#\[\.,]*$/;
    fe.filter = function(e, t, n) {
        var i = t[0];
        return n && (e = ":not(" + e + ")"), 1 === t.length && 1 === i.nodeType ? fe.find.matchesSelector(i, e) ? [i] : [] : fe.find.matches(e, fe.grep(t, function(e) {
            return 1 === e.nodeType
        }))
    }, fe.fn.extend({
        find: function(e) {
            var t, n = [],
                i = this,
                o = i.length;
            if ("string" != typeof e) return this.pushStack(fe(e).filter(function() {
                for (t = 0; t < o; t++)
                    if (fe.contains(i[t], this)) return !0
            }));
            for (t = 0; t < o; t++) fe.find(e, i[t], n);
            return n = this.pushStack(o > 1 ? fe.unique(n) : n), n.selector = this.selector ? this.selector + " " + e : e, n
        },
        filter: function(e) {
            return this.pushStack(i(this, e || [], !1))
        },
        not: function(e) {
            return this.pushStack(i(this, e || [], !0))
        },
        is: function(e) {
            return !!i(this, "string" == typeof e && Te.test(e) ? fe(e) : e || [], !1).length
        }
    });
    var De, ke = /^(?:\s*(<[\w\W]+>)[^>]*|#([\w-]*))$/,
        Se = fe.fn.init = function(e, t, n) {
            var i, o;
            if (!e) return this;
            if (n = n || De, "string" == typeof e) {
                if (i = "<" === e.charAt(0) && ">" === e.charAt(e.length - 1) && e.length >= 3 ? [null, e, null] : ke.exec(e), !i || !i[1] && t) return !t || t.jquery ? (t || n).find(e) : this.constructor(t).find(e);
                if (i[1]) {
                    if (t = t instanceof fe ? t[0] : t, fe.merge(this, fe.parseHTML(i[1], t && t.nodeType ? t.ownerDocument || t : ie, !0)), xe.test(i[1]) && fe.isPlainObject(t))
                        for (i in t) fe.isFunction(this[i]) ? this[i](t[i]) : this.attr(i, t[i]);
                    return this
                }
                if (o = ie.getElementById(i[2]), o && o.parentNode) {
                    if (o.id !== i[2]) return De.find(e);
                    this.length = 1, this[0] = o
                }
                return this.context = ie, this.selector = e, this
            }
            return e.nodeType ? (this.context = this[0] = e, this.length = 1, this) : fe.isFunction(e) ? "undefined" != typeof n.ready ? n.ready(e) : e(fe) : (void 0 !== e.selector && (this.selector = e.selector, this.context = e.context), fe.makeArray(e, this))
        };
    Se.prototype = fe.fn, De = fe(ie);
    var Ee = /^(?:parents|prev(?:Until|All))/,
        Me = {
            children: !0,
            contents: !0,
            next: !0,
            prev: !0
        };
    fe.fn.extend({
        has: function(e) {
            var t, n = fe(e, this),
                i = n.length;
            return this.filter(function() {
                for (t = 0; t < i; t++)
                    if (fe.contains(this, n[t])) return !0
            })
        },
        closest: function(e, t) {
            for (var n, i = 0, o = this.length, s = [], r = Te.test(e) || "string" != typeof e ? fe(e, t || this.context) : 0; i < o; i++)
                for (n = this[i]; n && n !== t; n = n.parentNode)
                    if (n.nodeType < 11 && (r ? r.index(n) > -1 : 1 === n.nodeType && fe.find.matchesSelector(n, e))) {
                        s.push(n);
                        break
                    }
            return this.pushStack(s.length > 1 ? fe.uniqueSort(s) : s)
        },
        index: function(e) {
            return e ? "string" == typeof e ? fe.inArray(this[0], fe(e)) : fe.inArray(e.jquery ? e[0] : e, this) : this[0] && this[0].parentNode ? this.first().prevAll().length : -1
        },
        add: function(e, t) {
            return this.pushStack(fe.uniqueSort(fe.merge(this.get(), fe(e, t))))
        },
        addBack: function(e) {
            return this.add(null == e ? this.prevObject : this.prevObject.filter(e))
        }
    }), fe.each({
        parent: function(e) {
            var t = e.parentNode;
            return t && 11 !== t.nodeType ? t : null
        },
        parents: function(e) {
            return be(e, "parentNode")
        },
        parentsUntil: function(e, t, n) {
            return be(e, "parentNode", n)
        },
        next: function(e) {
            return o(e, "nextSibling")
        },
        prev: function(e) {
            return o(e, "previousSibling")
        },
        nextAll: function(e) {
            return be(e, "nextSibling")
        },
        prevAll: function(e) {
            return be(e, "previousSibling")
        },
        nextUntil: function(e, t, n) {
            return be(e, "nextSibling", n)
        },
        prevUntil: function(e, t, n) {
            return be(e, "previousSibling", n)
        },
        siblings: function(e) {
            return we((e.parentNode || {}).firstChild, e)
        },
        children: function(e) {
            return we(e.firstChild)
        },
        contents: function(e) {
            return fe.nodeName(e, "iframe") ? e.contentDocument || e.contentWindow.document : fe.merge([], e.childNodes)
        }
    }, function(e, t) {
        fe.fn[e] = function(n, i) {
            var o = fe.map(this, t, n);
            return "Until" !== e.slice(-5) && (i = n), i && "string" == typeof i && (o = fe.filter(i, o)), this.length > 1 && (Me[e] || (o = fe.uniqueSort(o)), Ee.test(e) && (o = o.reverse())), this.pushStack(o)
        }
    });
    var Ne = /\S+/g;
    fe.Callbacks = function(e) {
        e = "string" == typeof e ? s(e) : fe.extend({}, e);
        var t, n, i, o, r = [],
            a = [],
            l = -1,
            c = function() {
                for (o = e.once, i = t = !0; a.length; l = -1)
                    for (n = a.shift(); ++l < r.length;) r[l].apply(n[0], n[1]) === !1 && e.stopOnFalse && (l = r.length, n = !1);
                e.memory || (n = !1), t = !1, o && (r = n ? [] : "")
            },
            u = {
                add: function() {
                    return r && (n && !t && (l = r.length - 1, a.push(n)), function i(t) {
                        fe.each(t, function(t, n) {
                            fe.isFunction(n) ? e.unique && u.has(n) || r.push(n) : n && n.length && "string" !== fe.type(n) && i(n)
                        })
                    }(arguments), n && !t && c()), this
                },
                remove: function() {
                    return fe.each(arguments, function(e, t) {
                        for (var n;
                            (n = fe.inArray(t, r, n)) > -1;) r.splice(n, 1), n <= l && l--
                    }), this
                },
                has: function(e) {
                    return e ? fe.inArray(e, r) > -1 : r.length > 0
                },
                empty: function() {
                    return r && (r = []), this
                },
                disable: function() {
                    return o = a = [], r = n = "", this
                },
                disabled: function() {
                    return !r
                },
                lock: function() {
                    return o = !0, n || u.disable(), this
                },
                locked: function() {
                    return !!o
                },
                fireWith: function(e, n) {
                    return o || (n = n || [], n = [e, n.slice ? n.slice() : n], a.push(n), t || c()), this
                },
                fire: function() {
                    return u.fireWith(this, arguments), this
                },
                fired: function() {
                    return !!i
                }
            };
        return u
    }, fe.extend({
        Deferred: function(e) {
            var t = [
                    ["resolve", "done", fe.Callbacks("once memory"), "resolved"],
                    ["reject", "fail", fe.Callbacks("once memory"), "rejected"],
                    ["notify", "progress", fe.Callbacks("memory")]
                ],
                n = "pending",
                i = {
                    state: function() {
                        return n
                    },
                    always: function() {
                        return o.done(arguments).fail(arguments), this
                    },
                    then: function() {
                        var e = arguments;
                        return fe.Deferred(function(n) {
                            fe.each(t, function(t, s) {
                                var r = fe.isFunction(e[t]) && e[t];
                                o[s[1]](function() {
                                    var e = r && r.apply(this, arguments);
                                    e && fe.isFunction(e.promise) ? e.promise().progress(n.notify).done(n.resolve).fail(n.reject) : n[s[0] + "With"](this === i ? n.promise() : this, r ? [e] : arguments)
                                })
                            }), e = null
                        }).promise()
                    },
                    promise: function(e) {
                        return null != e ? fe.extend(e, i) : i
                    }
                },
                o = {};
            return i.pipe = i.then, fe.each(t, function(e, s) {
                var r = s[2],
                    a = s[3];
                i[s[1]] = r.add, a && r.add(function() {
                    n = a
                }, t[1 ^ e][2].disable, t[2][2].lock), o[s[0]] = function() {
                    return o[s[0] + "With"](this === o ? i : this, arguments), this
                }, o[s[0] + "With"] = r.fireWith
            }), i.promise(o), e && e.call(o, o), o
        },
        when: function(e) {
            var t, n, i, o = 0,
                s = oe.call(arguments),
                r = s.length,
                a = 1 !== r || e && fe.isFunction(e.promise) ? r : 0,
                l = 1 === a ? e : fe.Deferred(),
                c = function(e, n, i) {
                    return function(o) {
                        n[e] = this, i[e] = arguments.length > 1 ? oe.call(arguments) : o, i === t ? l.notifyWith(n, i) : --a || l.resolveWith(n, i)
                    }
                };
            if (r > 1)
                for (t = new Array(r), n = new Array(r), i = new Array(r); o < r; o++) s[o] && fe.isFunction(s[o].promise) ? s[o].promise().progress(c(o, n, t)).done(c(o, i, s)).fail(l.reject) : --a;
            return a || l.resolveWith(i, s), l.promise()
        }
    });
    var Ae;
    fe.fn.ready = function(e) {
        return fe.ready.promise().done(e), this
    }, fe.extend({
        isReady: !1,
        readyWait: 1,
        holdReady: function(e) {
            e ? fe.readyWait++ : fe.ready(!0)
        },
        ready: function(e) {
            (e === !0 ? --fe.readyWait : fe.isReady) || (fe.isReady = !0, e !== !0 && --fe.readyWait > 0 || (Ae.resolveWith(ie, [fe]), fe.fn.triggerHandler && (fe(ie).triggerHandler("ready"), fe(ie).off("ready"))))
        }
    }), fe.ready.promise = function(t) {
        if (!Ae)
            if (Ae = fe.Deferred(), "complete" === ie.readyState || "loading" !== ie.readyState && !ie.documentElement.doScroll) e.setTimeout(fe.ready);
            else if (ie.addEventListener) ie.addEventListener("DOMContentLoaded", a), e.addEventListener("load", a);
        else {
            ie.attachEvent("onreadystatechange", a), e.attachEvent("onload", a);
            var n = !1;
            try {
                n = null == e.frameElement && ie.documentElement
            } catch (i) {}
            n && n.doScroll && ! function o() {
                if (!fe.isReady) {
                    try {
                        n.doScroll("left")
                    } catch (t) {
                        return e.setTimeout(o, 50)
                    }
                    r(), fe.ready()
                }
            }()
        }
        return Ae.promise(t)
    }, fe.ready.promise();
    var Ie;
    for (Ie in fe(de)) break;
    de.ownFirst = "0" === Ie, de.inlineBlockNeedsLayout = !1, fe(function() {
            var e, t, n, i;
            n = ie.getElementsByTagName("body")[0], n && n.style && (t = ie.createElement("div"), i = ie.createElement("div"), i.style.cssText = "position:absolute;border:0;width:0;height:0;top:0;left:-9999px", n.appendChild(i).appendChild(t), "undefined" != typeof t.style.zoom && (t.style.cssText = "display:inline;margin:0;border:0;padding:1px;width:1px;zoom:1", de.inlineBlockNeedsLayout = e = 3 === t.offsetWidth, e && (n.style.zoom = 1)), n.removeChild(i))
        }),
        function() {
            var e = ie.createElement("div");
            de.deleteExpando = !0;
            try {
                delete e.test
            } catch (t) {
                de.deleteExpando = !1
            }
            e = null
        }();
    var Oe = function(e) {
            var t = fe.noData[(e.nodeName + " ").toLowerCase()],
                n = +e.nodeType || 1;
            return (1 === n || 9 === n) && (!t || t !== !0 && e.getAttribute("classid") === t)
        },
        $e = /^(?:\{[\w\W]*\}|\[[\w\W]*\])$/,
        Ue = /([A-Z])/g;
    fe.extend({
            cache: {},
            noData: {
                "applet ": !0,
                "embed ": !0,
                "object ": "clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
            },
            hasData: function(e) {
                return e = e.nodeType ? fe.cache[e[fe.expando]] : e[fe.expando], !!e && !c(e)
            },
            data: function(e, t, n) {
                return u(e, t, n)
            },
            removeData: function(e, t) {
                return d(e, t)
            },
            _data: function(e, t, n) {
                return u(e, t, n, !0)
            },
            _removeData: function(e, t) {
                return d(e, t, !0)
            }
        }), fe.fn.extend({
            data: function(e, t) {
                var n, i, o, s = this[0],
                    r = s && s.attributes;
                if (void 0 === e) {
                    if (this.length && (o = fe.data(s), 1 === s.nodeType && !fe._data(s, "parsedAttrs"))) {
                        for (n = r.length; n--;) r[n] && (i = r[n].name, 0 === i.indexOf("data-") && (i = fe.camelCase(i.slice(5)), l(s, i, o[i])));
                        fe._data(s, "parsedAttrs", !0)
                    }
                    return o
                }
                return "object" == typeof e ? this.each(function() {
                    fe.data(this, e)
                }) : arguments.length > 1 ? this.each(function() {
                    fe.data(this, e, t)
                }) : s ? l(s, e, fe.data(s, e)) : void 0
            },
            removeData: function(e) {
                return this.each(function() {
                    fe.removeData(this, e)
                })
            }
        }), fe.extend({
            queue: function(e, t, n) {
                var i;
                if (e) return t = (t || "fx") + "queue", i = fe._data(e, t), n && (!i || fe.isArray(n) ? i = fe._data(e, t, fe.makeArray(n)) : i.push(n)), i || []
            },
            dequeue: function(e, t) {
                t = t || "fx";
                var n = fe.queue(e, t),
                    i = n.length,
                    o = n.shift(),
                    s = fe._queueHooks(e, t),
                    r = function() {
                        fe.dequeue(e, t)
                    };
                "inprogress" === o && (o = n.shift(), i--), o && ("fx" === t && n.unshift("inprogress"), delete s.stop, o.call(e, r, s)), !i && s && s.empty.fire()
            },
            _queueHooks: function(e, t) {
                var n = t + "queueHooks";
                return fe._data(e, n) || fe._data(e, n, {
                    empty: fe.Callbacks("once memory").add(function() {
                        fe._removeData(e, t + "queue"), fe._removeData(e, n)
                    })
                })
            }
        }), fe.fn.extend({
            queue: function(e, t) {
                var n = 2;
                return "string" != typeof e && (t = e, e = "fx", n--), arguments.length < n ? fe.queue(this[0], e) : void 0 === t ? this : this.each(function() {
                    var n = fe.queue(this, e, t);
                    fe._queueHooks(this, e), "fx" === e && "inprogress" !== n[0] && fe.dequeue(this, e)
                })
            },
            dequeue: function(e) {
                return this.each(function() {
                    fe.dequeue(this, e)
                })
            },
            clearQueue: function(e) {
                return this.queue(e || "fx", [])
            },
            promise: function(e, t) {
                var n, i = 1,
                    o = fe.Deferred(),
                    s = this,
                    r = this.length,
                    a = function() {
                        --i || o.resolveWith(s, [s])
                    };
                for ("string" != typeof e && (t = e, e = void 0), e = e || "fx"; r--;) n = fe._data(s[r], e + "queueHooks"), n && n.empty && (i++, n.empty.add(a));
                return a(), o.promise(t)
            }
        }),
        function() {
            var e;
            de.shrinkWrapBlocks = function() {
                if (null != e) return e;
                e = !1;
                var t, n, i;
                return n = ie.getElementsByTagName("body")[0], n && n.style ? (t = ie.createElement("div"), i = ie.createElement("div"), i.style.cssText = "position:absolute;border:0;width:0;height:0;top:0;left:-9999px", n.appendChild(i).appendChild(t), "undefined" != typeof t.style.zoom && (t.style.cssText = "-webkit-box-sizing:content-box;-moz-box-sizing:content-box;box-sizing:content-box;display:block;margin:0;border:0;padding:1px;width:1px;zoom:1", t.appendChild(ie.createElement("div")).style.width = "5px", e = 3 !== t.offsetWidth), n.removeChild(i), e) : void 0
            }
        }();
    var je = /[+-]?(?:\d*\.|)\d+(?:[eE][+-]?\d+|)/.source,
        Le = new RegExp("^(?:([+-])=|)(" + je + ")([a-z%]*)$", "i"),
        He = ["Top", "Right", "Bottom", "Left"],
        Fe = function(e, t) {
            return e = t || e, "none" === fe.css(e, "display") || !fe.contains(e.ownerDocument, e)
        },
        Re = function(e, t, n, i, o, s, r) {
            var a = 0,
                l = e.length,
                c = null == n;
            if ("object" === fe.type(n)) {
                o = !0;
                for (a in n) Re(e, t, a, n[a], !0, s, r)
            } else if (void 0 !== i && (o = !0, fe.isFunction(i) || (r = !0), c && (r ? (t.call(e, i), t = null) : (c = t, t = function(e, t, n) {
                return c.call(fe(e), n)
            })), t))
                for (; a < l; a++) t(e[a], n, r ? i : i.call(e[a], a, t(e[a], n)));
            return o ? e : c ? t.call(e) : l ? t(e[0], n) : s
        },
        Pe = /^(?:checkbox|radio)$/i,
        _e = /<([\w:-]+)/,
        qe = /^$|\/(?:java|ecma)script/i,
        Be = /^\s+/,
        Ve = "abbr|article|aside|audio|bdi|canvas|data|datalist|details|dialog|figcaption|figure|footer|header|hgroup|main|mark|meter|nav|output|picture|progress|section|summary|template|time|video";
    ! function() {
        var e = ie.createElement("div"),
            t = ie.createDocumentFragment(),
            n = ie.createElement("input");
        e.innerHTML = "  <link/><table></table><a href='/a'>a</a><input type='checkbox'/>", de.leadingWhitespace = 3 === e.firstChild.nodeType, de.tbody = !e.getElementsByTagName("tbody").length, de.htmlSerialize = !!e.getElementsByTagName("link").length, de.html5Clone = "<:nav></:nav>" !== ie.createElement("nav").cloneNode(!0).outerHTML, n.type = "checkbox", n.checked = !0, t.appendChild(n), de.appendChecked = n.checked, e.innerHTML = "<textarea>x</textarea>", de.noCloneChecked = !!e.cloneNode(!0).lastChild.defaultValue, t.appendChild(e), n = ie.createElement("input"), n.setAttribute("type", "radio"), n.setAttribute("checked", "checked"), n.setAttribute("name", "t"), e.appendChild(n), de.checkClone = e.cloneNode(!0).cloneNode(!0).lastChild.checked, de.noCloneEvent = !!e.addEventListener, e[fe.expando] = 1, de.attributes = !e.getAttribute(fe.expando)
    }();
    var We = {
        option: [1, "<select multiple='multiple'>", "</select>"],
        legend: [1, "<fieldset>", "</fieldset>"],
        area: [1, "<map>", "</map>"],
        param: [1, "<object>", "</object>"],
        thead: [1, "<table>", "</table>"],
        tr: [2, "<table><tbody>", "</tbody></table>"],
        col: [2, "<table><tbody></tbody><colgroup>", "</colgroup></table>"],
        td: [3, "<table><tbody><tr>", "</tr></tbody></table>"],
        _default: de.htmlSerialize ? [0, "", ""] : [1, "X<div>", "</div>"]
    };
    We.optgroup = We.option, We.tbody = We.tfoot = We.colgroup = We.caption = We.thead, We.th = We.td;
    var ze = /<|&#?\w+;/,
        Ye = /<tbody/i;
    ! function() {
        var t, n, i = ie.createElement("div");
        for (t in {
            submit: !0,
            change: !0,
            focusin: !0
        }) n = "on" + t, (de[t] = n in e) || (i.setAttribute(n, "t"), de[t] = i.attributes[n].expando === !1);
        i = null
    }();
    var Qe = /^(?:input|select|textarea)$/i,
        Xe = /^key/,
        Je = /^(?:mouse|pointer|contextmenu|drag|drop)|click/,
        Ge = /^(?:focusinfocus|focusoutblur)$/,
        Ke = /^([^.]*)(?:\.(.+)|)/;
    fe.event = {
        global: {},
        add: function(e, t, n, i, o) {
            var s, r, a, l, c, u, d, h, f, p, m, g = fe._data(e);
            if (g) {
                for (n.handler && (l = n, n = l.handler, o = l.selector), n.guid || (n.guid = fe.guid++), (r = g.events) || (r = g.events = {}), (u = g.handle) || (u = g.handle = function(e) {
                    return "undefined" == typeof fe || e && fe.event.triggered === e.type ? void 0 : fe.event.dispatch.apply(u.elem, arguments)
                }, u.elem = e), t = (t || "").match(Ne) || [""], a = t.length; a--;) s = Ke.exec(t[a]) || [], f = m = s[1], p = (s[2] || "").split(".").sort(), f && (c = fe.event.special[f] || {}, f = (o ? c.delegateType : c.bindType) || f, c = fe.event.special[f] || {}, d = fe.extend({
                    type: f,
                    origType: m,
                    data: i,
                    handler: n,
                    guid: n.guid,
                    selector: o,
                    needsContext: o && fe.expr.match.needsContext.test(o),
                    namespace: p.join(".")
                }, l), (h = r[f]) || (h = r[f] = [], h.delegateCount = 0, c.setup && c.setup.call(e, i, p, u) !== !1 || (e.addEventListener ? e.addEventListener(f, u, !1) : e.attachEvent && e.attachEvent("on" + f, u))), c.add && (c.add.call(e, d), d.handler.guid || (d.handler.guid = n.guid)), o ? h.splice(h.delegateCount++, 0, d) : h.push(d), fe.event.global[f] = !0);
                e = null
            }
        },
        remove: function(e, t, n, i, o) {
            var s, r, a, l, c, u, d, h, f, p, m, g = fe.hasData(e) && fe._data(e);
            if (g && (u = g.events)) {
                for (t = (t || "").match(Ne) || [""], c = t.length; c--;)
                    if (a = Ke.exec(t[c]) || [], f = m = a[1], p = (a[2] || "").split(".").sort(), f) {
                        for (d = fe.event.special[f] || {}, f = (i ? d.delegateType : d.bindType) || f, h = u[f] || [], a = a[2] && new RegExp("(^|\\.)" + p.join("\\.(?:.*\\.|)") + "(\\.|$)"), l = s = h.length; s--;) r = h[s], !o && m !== r.origType || n && n.guid !== r.guid || a && !a.test(r.namespace) || i && i !== r.selector && ("**" !== i || !r.selector) || (h.splice(s, 1), r.selector && h.delegateCount--, d.remove && d.remove.call(e, r));
                        l && !h.length && (d.teardown && d.teardown.call(e, p, g.handle) !== !1 || fe.removeEvent(e, f, g.handle), delete u[f])
                    } else
                        for (f in u) fe.event.remove(e, f + t[c], n, i, !0);
                fe.isEmptyObject(u) && (delete g.handle, fe._removeData(e, "events"))
            }
        },
        trigger: function(t, n, i, o) {
            var s, r, a, l, c, u, d, h = [i || ie],
                f = ue.call(t, "type") ? t.type : t,
                p = ue.call(t, "namespace") ? t.namespace.split(".") : [];
            if (a = u = i = i || ie, 3 !== i.nodeType && 8 !== i.nodeType && !Ge.test(f + fe.event.triggered) && (f.indexOf(".") > -1 && (p = f.split("."), f = p.shift(), p.sort()), r = f.indexOf(":") < 0 && "on" + f, t = t[fe.expando] ? t : new fe.Event(f, "object" == typeof t && t), t.isTrigger = o ? 2 : 3, t.namespace = p.join("."), t.rnamespace = t.namespace ? new RegExp("(^|\\.)" + p.join("\\.(?:.*\\.|)") + "(\\.|$)") : null, t.result = void 0, t.target || (t.target = i), n = null == n ? [t] : fe.makeArray(n, [t]), c = fe.event.special[f] || {}, o || !c.trigger || c.trigger.apply(i, n) !== !1)) {
                if (!o && !c.noBubble && !fe.isWindow(i)) {
                    for (l = c.delegateType || f, Ge.test(l + f) || (a = a.parentNode); a; a = a.parentNode) h.push(a), u = a;
                    u === (i.ownerDocument || ie) && h.push(u.defaultView || u.parentWindow || e)
                }
                for (d = 0;
                    (a = h[d++]) && !t.isPropagationStopped();) t.type = d > 1 ? l : c.bindType || f, s = (fe._data(a, "events") || {})[t.type] && fe._data(a, "handle"), s && s.apply(a, n), s = r && a[r], s && s.apply && Oe(a) && (t.result = s.apply(a, n), t.result === !1 && t.preventDefault());
                if (t.type = f, !o && !t.isDefaultPrevented() && (!c._default || c._default.apply(h.pop(), n) === !1) && Oe(i) && r && i[f] && !fe.isWindow(i)) {
                    u = i[r], u && (i[r] = null), fe.event.triggered = f;
                    try {
                        i[f]()
                    } catch (m) {}
                    fe.event.triggered = void 0, u && (i[r] = u)
                }
                return t.result
            }
        },
        dispatch: function(e) {
            e = fe.event.fix(e);
            var t, n, i, o, s, r = [],
                a = oe.call(arguments),
                l = (fe._data(this, "events") || {})[e.type] || [],
                c = fe.event.special[e.type] || {};
            if (a[0] = e, e.delegateTarget = this, !c.preDispatch || c.preDispatch.call(this, e) !== !1) {
                for (r = fe.event.handlers.call(this, e, l), t = 0;
                    (o = r[t++]) && !e.isPropagationStopped();)
                    for (e.currentTarget = o.elem, n = 0;
                        (s = o.handlers[n++]) && !e.isImmediatePropagationStopped();) e.rnamespace && !e.rnamespace.test(s.namespace) || (e.handleObj = s, e.data = s.data, i = ((fe.event.special[s.origType] || {}).handle || s.handler).apply(o.elem, a), void 0 !== i && (e.result = i) === !1 && (e.preventDefault(), e.stopPropagation()));
                return c.postDispatch && c.postDispatch.call(this, e), e.result
            }
        },
        handlers: function(e, t) {
            var n, i, o, s, r = [],
                a = t.delegateCount,
                l = e.target;
            if (a && l.nodeType && ("click" !== e.type || isNaN(e.button) || e.button < 1))
                for (; l != this; l = l.parentNode || this)
                    if (1 === l.nodeType && (l.disabled !== !0 || "click" !== e.type)) {
                        for (i = [], n = 0; n < a; n++) s = t[n], o = s.selector + " ", void 0 === i[o] && (i[o] = s.needsContext ? fe(o, this).index(l) > -1 : fe.find(o, this, null, [l]).length), i[o] && i.push(s);
                        i.length && r.push({
                            elem: l,
                            handlers: i
                        })
                    }
            return a < t.length && r.push({
                elem: this,
                handlers: t.slice(a)
            }), r
        },
        fix: function(e) {
            if (e[fe.expando]) return e;
            var t, n, i, o = e.type,
                s = e,
                r = this.fixHooks[o];
            for (r || (this.fixHooks[o] = r = Je.test(o) ? this.mouseHooks : Xe.test(o) ? this.keyHooks : {}), i = r.props ? this.props.concat(r.props) : this.props, e = new fe.Event(s), t = i.length; t--;) n = i[t], e[n] = s[n];
            return e.target || (e.target = s.srcElement || ie), 3 === e.target.nodeType && (e.target = e.target.parentNode), e.metaKey = !!e.metaKey, r.filter ? r.filter(e, s) : e
        },
        props: "altKey bubbles cancelable ctrlKey currentTarget detail eventPhase metaKey relatedTarget shiftKey target timeStamp view which".split(" "),
        fixHooks: {},
        keyHooks: {
            props: "char charCode key keyCode".split(" "),
            filter: function(e, t) {
                return null == e.which && (e.which = null != t.charCode ? t.charCode : t.keyCode), e
            }
        },
        mouseHooks: {
            props: "button buttons clientX clientY fromElement offsetX offsetY pageX pageY screenX screenY toElement".split(" "),
            filter: function(e, t) {
                var n, i, o, s = t.button,
                    r = t.fromElement;
                return null == e.pageX && null != t.clientX && (i = e.target.ownerDocument || ie, o = i.documentElement, n = i.body, e.pageX = t.clientX + (o && o.scrollLeft || n && n.scrollLeft || 0) - (o && o.clientLeft || n && n.clientLeft || 0), e.pageY = t.clientY + (o && o.scrollTop || n && n.scrollTop || 0) - (o && o.clientTop || n && n.clientTop || 0)), !e.relatedTarget && r && (e.relatedTarget = r === e.target ? t.toElement : r), e.which || void 0 === s || (e.which = 1 & s ? 1 : 2 & s ? 3 : 4 & s ? 2 : 0), e
            }
        },
        special: {
            load: {
                noBubble: !0
            },
            focus: {
                trigger: function() {
                    if (this !== w() && this.focus) try {
                        return this.focus(), !1
                    } catch (e) {}
                },
                delegateType: "focusin"
            },
            blur: {
                trigger: function() {
                    if (this === w() && this.blur) return this.blur(), !1
                },
                delegateType: "focusout"
            },
            click: {
                trigger: function() {
                    if (fe.nodeName(this, "input") && "checkbox" === this.type && this.click) return this.click(), !1
                },
                _default: function(e) {
                    return fe.nodeName(e.target, "a")
                }
            },
            beforeunload: {
                postDispatch: function(e) {
                    void 0 !== e.result && e.originalEvent && (e.originalEvent.returnValue = e.result)
                }
            }
        },
        simulate: function(e, t, n) {
            var i = fe.extend(new fe.Event, n, {
                type: e,
                isSimulated: !0
            });
            fe.event.trigger(i, null, t), i.isDefaultPrevented() && n.preventDefault()
        }
    }, fe.removeEvent = ie.removeEventListener ? function(e, t, n) {
        e.removeEventListener && e.removeEventListener(t, n)
    } : function(e, t, n) {
        var i = "on" + t;
        e.detachEvent && ("undefined" == typeof e[i] && (e[i] = null), e.detachEvent(i, n))
    }, fe.Event = function(e, t) {
        return this instanceof fe.Event ? (e && e.type ? (this.originalEvent = e, this.type = e.type, this.isDefaultPrevented = e.defaultPrevented || void 0 === e.defaultPrevented && e.returnValue === !1 ? y : b) : this.type = e, t && fe.extend(this, t), this.timeStamp = e && e.timeStamp || fe.now(), void(this[fe.expando] = !0)) : new fe.Event(e, t)
    }, fe.Event.prototype = {
        constructor: fe.Event,
        isDefaultPrevented: b,
        isPropagationStopped: b,
        isImmediatePropagationStopped: b,
        preventDefault: function() {
            var e = this.originalEvent;
            this.isDefaultPrevented = y, e && (e.preventDefault ? e.preventDefault() : e.returnValue = !1)
        },
        stopPropagation: function() {
            var e = this.originalEvent;
            this.isPropagationStopped = y, e && !this.isSimulated && (e.stopPropagation && e.stopPropagation(), e.cancelBubble = !0)
        },
        stopImmediatePropagation: function() {
            var e = this.originalEvent;
            this.isImmediatePropagationStopped = y, e && e.stopImmediatePropagation && e.stopImmediatePropagation(), this.stopPropagation()
        }
    }, fe.each({
        mouseenter: "mouseover",
        mouseleave: "mouseout",
        pointerenter: "pointerover",
        pointerleave: "pointerout"
    }, function(e, t) {
        fe.event.special[e] = {
            delegateType: t,
            bindType: t,
            handle: function(e) {
                var n, i = this,
                    o = e.relatedTarget,
                    s = e.handleObj;
                return o && (o === i || fe.contains(i, o)) || (e.type = s.origType, n = s.handler.apply(this, arguments), e.type = t), n
            }
        }
    }), de.submit || (fe.event.special.submit = {
        setup: function() {
            return !fe.nodeName(this, "form") && void fe.event.add(this, "click._submit keypress._submit", function(e) {
                var t = e.target,
                    n = fe.nodeName(t, "input") || fe.nodeName(t, "button") ? fe.prop(t, "form") : void 0;
                n && !fe._data(n, "submit") && (fe.event.add(n, "submit._submit", function(e) {
                    e._submitBubble = !0
                }), fe._data(n, "submit", !0))
            })
        },
        postDispatch: function(e) {
            e._submitBubble && (delete e._submitBubble, this.parentNode && !e.isTrigger && fe.event.simulate("submit", this.parentNode, e))
        },
        teardown: function() {
            return !fe.nodeName(this, "form") && void fe.event.remove(this, "._submit")
        }
    }), de.change || (fe.event.special.change = {
        setup: function() {
            return Qe.test(this.nodeName) ? ("checkbox" !== this.type && "radio" !== this.type || (fe.event.add(this, "propertychange._change", function(e) {
                "checked" === e.originalEvent.propertyName && (this._justChanged = !0)
            }), fe.event.add(this, "click._change", function(e) {
                this._justChanged && !e.isTrigger && (this._justChanged = !1), fe.event.simulate("change", this, e)
            })), !1) : void fe.event.add(this, "beforeactivate._change", function(e) {
                var t = e.target;
                Qe.test(t.nodeName) && !fe._data(t, "change") && (fe.event.add(t, "change._change", function(e) {
                    !this.parentNode || e.isSimulated || e.isTrigger || fe.event.simulate("change", this.parentNode, e)
                }), fe._data(t, "change", !0))
            })
        },
        handle: function(e) {
            var t = e.target;
            if (this !== t || e.isSimulated || e.isTrigger || "radio" !== t.type && "checkbox" !== t.type) return e.handleObj.handler.apply(this, arguments)
        },
        teardown: function() {
            return fe.event.remove(this, "._change"), !Qe.test(this.nodeName)
        }
    }), de.focusin || fe.each({
        focus: "focusin",
        blur: "focusout"
    }, function(e, t) {
        var n = function(e) {
            fe.event.simulate(t, e.target, fe.event.fix(e))
        };
        fe.event.special[t] = {
            setup: function() {
                var i = this.ownerDocument || this,
                    o = fe._data(i, t);
                o || i.addEventListener(e, n, !0), fe._data(i, t, (o || 0) + 1)
            },
            teardown: function() {
                var i = this.ownerDocument || this,
                    o = fe._data(i, t) - 1;
                o ? fe._data(i, t, o) : (i.removeEventListener(e, n, !0), fe._removeData(i, t))
            }
        }
    }), fe.fn.extend({
        on: function(e, t, n, i) {
            return T(this, e, t, n, i)
        },
        one: function(e, t, n, i) {
            return T(this, e, t, n, i, 1)
        },
        off: function(e, t, n) {
            var i, o;
            if (e && e.preventDefault && e.handleObj) return i = e.handleObj, fe(e.delegateTarget).off(i.namespace ? i.origType + "." + i.namespace : i.origType, i.selector, i.handler), this;
            if ("object" == typeof e) {
                for (o in e) this.off(o, t, e[o]);
                return this
            }
            return t !== !1 && "function" != typeof t || (n = t, t = void 0), n === !1 && (n = b), this.each(function() {
                fe.event.remove(this, e, n, t)
            })
        },
        trigger: function(e, t) {
            return this.each(function() {
                fe.event.trigger(e, t, this)
            })
        },
        triggerHandler: function(e, t) {
            var n = this[0];
            if (n) return fe.event.trigger(e, t, n, !0)
        }
    });
    var Ze = / jQuery\d+="(?:null|\d+)"/g,
        et = new RegExp("<(?:" + Ve + ")[\\s/>]", "i"),
        tt = /<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:-]+)[^>]*)\/>/gi,
        nt = /<script|<style|<link/i,
        it = /checked\s*(?:[^=]|=\s*.checked.)/i,
        ot = /^true\/(.*)/,
        st = /^\s*<!(?:\[CDATA\[|--)|(?:\]\]|--)>\s*$/g,
        rt = f(ie),
        at = rt.appendChild(ie.createElement("div"));
    fe.extend({
        htmlPrefilter: function(e) {
            return e.replace(tt, "<$1></$2>")
        },
        clone: function(e, t, n) {
            var i, o, s, r, a, l = fe.contains(e.ownerDocument, e);
            if (de.html5Clone || fe.isXMLDoc(e) || !et.test("<" + e.nodeName + ">") ? s = e.cloneNode(!0) : (at.innerHTML = e.outerHTML, at.removeChild(s = at.firstChild)), !(de.noCloneEvent && de.noCloneChecked || 1 !== e.nodeType && 11 !== e.nodeType || fe.isXMLDoc(e)))
                for (i = p(s), a = p(e), r = 0; null != (o = a[r]); ++r) i[r] && S(o, i[r]);
            if (t)
                if (n)
                    for (a = a || p(e), i = i || p(s), r = 0; null != (o = a[r]); r++) k(o, i[r]);
                else k(e, s);
            return i = p(s, "script"), i.length > 0 && m(i, !l && p(e, "script")), i = a = o = null, s
        },
        cleanData: function(e, t) {
            for (var n, i, o, s, r = 0, a = fe.expando, l = fe.cache, c = de.attributes, u = fe.event.special; null != (n = e[r]); r++)
                if ((t || Oe(n)) && (o = n[a], s = o && l[o])) {
                    if (s.events)
                        for (i in s.events) u[i] ? fe.event.remove(n, i) : fe.removeEvent(n, i, s.handle);
                    l[o] && (delete l[o], c || "undefined" == typeof n.removeAttribute ? n[a] = void 0 : n.removeAttribute(a), ne.push(o))
                }
        }
    }), fe.fn.extend({
        domManip: E,
        detach: function(e) {
            return M(this, e, !0)
        },
        remove: function(e) {
            return M(this, e)
        },
        text: function(e) {
            return Re(this, function(e) {
                return void 0 === e ? fe.text(this) : this.empty().append((this[0] && this[0].ownerDocument || ie).createTextNode(e))
            }, null, e, arguments.length)
        },
        append: function() {
            return E(this, arguments, function(e) {
                if (1 === this.nodeType || 11 === this.nodeType || 9 === this.nodeType) {
                    var t = x(this, e);
                    t.appendChild(e)
                }
            })
        },
        prepend: function() {
            return E(this, arguments, function(e) {
                if (1 === this.nodeType || 11 === this.nodeType || 9 === this.nodeType) {
                    var t = x(this, e);
                    t.insertBefore(e, t.firstChild)
                }
            })
        },
        before: function() {
            return E(this, arguments, function(e) {
                this.parentNode && this.parentNode.insertBefore(e, this)
            })
        },
        after: function() {
            return E(this, arguments, function(e) {
                this.parentNode && this.parentNode.insertBefore(e, this.nextSibling)
            })
        },
        empty: function() {
            for (var e, t = 0; null != (e = this[t]); t++) {
                for (1 === e.nodeType && fe.cleanData(p(e, !1)); e.firstChild;) e.removeChild(e.firstChild);
                e.options && fe.nodeName(e, "select") && (e.options.length = 0)
            }
            return this
        },
        clone: function(e, t) {
            return e = null != e && e, t = null == t ? e : t, this.map(function() {
                return fe.clone(this, e, t)
            })
        },
        html: function(e) {
            return Re(this, function(e) {
                var t = this[0] || {},
                    n = 0,
                    i = this.length;
                if (void 0 === e) return 1 === t.nodeType ? t.innerHTML.replace(Ze, "") : void 0;
                if ("string" == typeof e && !nt.test(e) && (de.htmlSerialize || !et.test(e)) && (de.leadingWhitespace || !Be.test(e)) && !We[(_e.exec(e) || ["", ""])[1].toLowerCase()]) {
                    e = fe.htmlPrefilter(e);
                    try {
                        for (; n < i; n++) t = this[n] || {}, 1 === t.nodeType && (fe.cleanData(p(t, !1)), t.innerHTML = e);
                        t = 0
                    } catch (o) {}
                }
                t && this.empty().append(e)
            }, null, e, arguments.length)
        },
        replaceWith: function() {
            var e = [];
            return E(this, arguments, function(t) {
                var n = this.parentNode;
                fe.inArray(this, e) < 0 && (fe.cleanData(p(this)), n && n.replaceChild(t, this));
            }, e)
        }
    }), fe.each({
        appendTo: "append",
        prependTo: "prepend",
        insertBefore: "before",
        insertAfter: "after",
        replaceAll: "replaceWith"
    }, function(e, t) {
        fe.fn[e] = function(e) {
            for (var n, i = 0, o = [], s = fe(e), r = s.length - 1; i <= r; i++) n = i === r ? this : this.clone(!0), fe(s[i])[t](n), re.apply(o, n.get());
            return this.pushStack(o)
        }
    });
    var lt, ct = {
            HTML: "block",
            BODY: "block"
        },
        ut = /^margin/,
        dt = new RegExp("^(" + je + ")(?!px)[a-z%]+$", "i"),
        ht = function(e, t, n, i) {
            var o, s, r = {};
            for (s in t) r[s] = e.style[s], e.style[s] = t[s];
            o = n.apply(e, i || []);
            for (s in t) e.style[s] = r[s];
            return o
        },
        ft = ie.documentElement;
    ! function() {
        function t() {
            var t, u, d = ie.documentElement;
            d.appendChild(l), c.style.cssText = "-webkit-box-sizing:border-box;box-sizing:border-box;position:relative;display:block;margin:auto;border:1px;padding:1px;top:1%;width:50%", n = o = a = !1, i = r = !0, e.getComputedStyle && (u = e.getComputedStyle(c), n = "1%" !== (u || {}).top, a = "2px" === (u || {}).marginLeft, o = "4px" === (u || {
                width: "4px"
            }).width, c.style.marginRight = "50%", i = "4px" === (u || {
                marginRight: "4px"
            }).marginRight, t = c.appendChild(ie.createElement("div")), t.style.cssText = c.style.cssText = "-webkit-box-sizing:content-box;-moz-box-sizing:content-box;box-sizing:content-box;display:block;margin:0;border:0;padding:0", t.style.marginRight = t.style.width = "0", c.style.width = "1px", r = !parseFloat((e.getComputedStyle(t) || {}).marginRight), c.removeChild(t)), c.style.display = "none", s = 0 === c.getClientRects().length, s && (c.style.display = "", c.innerHTML = "<table><tr><td></td><td>t</td></tr></table>", c.childNodes[0].style.borderCollapse = "separate", t = c.getElementsByTagName("td"), t[0].style.cssText = "margin:0;border:0;padding:0;display:none", s = 0 === t[0].offsetHeight, s && (t[0].style.display = "", t[1].style.display = "none", s = 0 === t[0].offsetHeight)), d.removeChild(l)
        }
        var n, i, o, s, r, a, l = ie.createElement("div"),
            c = ie.createElement("div");
        c.style && (c.style.cssText = "float:left;opacity:.5", de.opacity = "0.5" === c.style.opacity, de.cssFloat = !!c.style.cssFloat, c.style.backgroundClip = "content-box", c.cloneNode(!0).style.backgroundClip = "", de.clearCloneStyle = "content-box" === c.style.backgroundClip, l = ie.createElement("div"), l.style.cssText = "border:0;width:8px;height:0;top:0;left:-9999px;padding:0;margin-top:1px;position:absolute", c.innerHTML = "", l.appendChild(c), de.boxSizing = "" === c.style.boxSizing || "" === c.style.MozBoxSizing || "" === c.style.WebkitBoxSizing, fe.extend(de, {
            reliableHiddenOffsets: function() {
                return null == n && t(), s
            },
            boxSizingReliable: function() {
                return null == n && t(), o
            },
            pixelMarginRight: function() {
                return null == n && t(), i
            },
            pixelPosition: function() {
                return null == n && t(), n
            },
            reliableMarginRight: function() {
                return null == n && t(), r
            },
            reliableMarginLeft: function() {
                return null == n && t(), a
            }
        }))
    }();
    var pt, mt, gt = /^(top|right|bottom|left)$/;
    e.getComputedStyle ? (pt = function(t) {
        var n = t.ownerDocument.defaultView;
        return n && n.opener || (n = e), n.getComputedStyle(t)
    }, mt = function(e, t, n) {
        var i, o, s, r, a = e.style;
        return n = n || pt(e), r = n ? n.getPropertyValue(t) || n[t] : void 0, "" !== r && void 0 !== r || fe.contains(e.ownerDocument, e) || (r = fe.style(e, t)), n && !de.pixelMarginRight() && dt.test(r) && ut.test(t) && (i = a.width, o = a.minWidth, s = a.maxWidth, a.minWidth = a.maxWidth = a.width = r, r = n.width, a.width = i, a.minWidth = o, a.maxWidth = s), void 0 === r ? r : r + ""
    }) : ft.currentStyle && (pt = function(e) {
        return e.currentStyle
    }, mt = function(e, t, n) {
        var i, o, s, r, a = e.style;
        return n = n || pt(e), r = n ? n[t] : void 0, null == r && a && a[t] && (r = a[t]), dt.test(r) && !gt.test(t) && (i = a.left, o = e.runtimeStyle, s = o && o.left, s && (o.left = e.currentStyle.left), a.left = "fontSize" === t ? "1em" : r, r = a.pixelLeft + "px", a.left = i, s && (o.left = s)), void 0 === r ? r : r + "" || "auto"
    });
    var vt = /alpha\([^)]*\)/i,
        yt = /opacity\s*=\s*([^)]*)/i,
        bt = /^(none|table(?!-c[ea]).+)/,
        wt = new RegExp("^(" + je + ")(.*)$", "i"),
        Tt = {
            position: "absolute",
            visibility: "hidden",
            display: "block"
        },
        xt = {
            letterSpacing: "0",
            fontWeight: "400"
        },
        Ct = ["Webkit", "O", "Moz", "ms"],
        Dt = ie.createElement("div").style;
    fe.extend({
        cssHooks: {
            opacity: {
                get: function(e, t) {
                    if (t) {
                        var n = mt(e, "opacity");
                        return "" === n ? "1" : n
                    }
                }
            }
        },
        cssNumber: {
            animationIterationCount: !0,
            columnCount: !0,
            fillOpacity: !0,
            flexGrow: !0,
            flexShrink: !0,
            fontWeight: !0,
            lineHeight: !0,
            opacity: !0,
            order: !0,
            orphans: !0,
            widows: !0,
            zIndex: !0,
            zoom: !0
        },
        cssProps: {
            "float": de.cssFloat ? "cssFloat" : "styleFloat"
        },
        style: function(e, t, n, i) {
            if (e && 3 !== e.nodeType && 8 !== e.nodeType && e.style) {
                var o, s, r, a = fe.camelCase(t),
                    l = e.style;
                if (t = fe.cssProps[a] || (fe.cssProps[a] = O(a) || a), r = fe.cssHooks[t] || fe.cssHooks[a], void 0 === n) return r && "get" in r && void 0 !== (o = r.get(e, !1, i)) ? o : l[t];
                if (s = typeof n, "string" === s && (o = Le.exec(n)) && o[1] && (n = h(e, t, o), s = "number"), null != n && n === n && ("number" === s && (n += o && o[3] || (fe.cssNumber[a] ? "" : "px")), de.clearCloneStyle || "" !== n || 0 !== t.indexOf("background") || (l[t] = "inherit"), !(r && "set" in r && void 0 === (n = r.set(e, n, i))))) try {
                    l[t] = n
                } catch (c) {}
            }
        },
        css: function(e, t, n, i) {
            var o, s, r, a = fe.camelCase(t);
            return t = fe.cssProps[a] || (fe.cssProps[a] = O(a) || a), r = fe.cssHooks[t] || fe.cssHooks[a], r && "get" in r && (s = r.get(e, !0, n)), void 0 === s && (s = mt(e, t, i)), "normal" === s && t in xt && (s = xt[t]), "" === n || n ? (o = parseFloat(s), n === !0 || isFinite(o) ? o || 0 : s) : s
        }
    }), fe.each(["height", "width"], function(e, t) {
        fe.cssHooks[t] = {
            get: function(e, n, i) {
                if (n) return bt.test(fe.css(e, "display")) && 0 === e.offsetWidth ? ht(e, Tt, function() {
                    return L(e, t, i)
                }) : L(e, t, i)
            },
            set: function(e, n, i) {
                var o = i && pt(e);
                return U(e, n, i ? j(e, t, i, de.boxSizing && "border-box" === fe.css(e, "boxSizing", !1, o), o) : 0)
            }
        }
    }), de.opacity || (fe.cssHooks.opacity = {
        get: function(e, t) {
            return yt.test((t && e.currentStyle ? e.currentStyle.filter : e.style.filter) || "") ? .01 * parseFloat(RegExp.$1) + "" : t ? "1" : ""
        },
        set: function(e, t) {
            var n = e.style,
                i = e.currentStyle,
                o = fe.isNumeric(t) ? "alpha(opacity=" + 100 * t + ")" : "",
                s = i && i.filter || n.filter || "";
            n.zoom = 1, (t >= 1 || "" === t) && "" === fe.trim(s.replace(vt, "")) && n.removeAttribute && (n.removeAttribute("filter"), "" === t || i && !i.filter) || (n.filter = vt.test(s) ? s.replace(vt, o) : s + " " + o)
        }
    }), fe.cssHooks.marginRight = I(de.reliableMarginRight, function(e, t) {
        if (t) return ht(e, {
            display: "inline-block"
        }, mt, [e, "marginRight"])
    }), fe.cssHooks.marginLeft = I(de.reliableMarginLeft, function(e, t) {
        if (t) return (parseFloat(mt(e, "marginLeft")) || (fe.contains(e.ownerDocument, e) ? e.getBoundingClientRect().left - ht(e, {
            marginLeft: 0
        }, function() {
            return e.getBoundingClientRect().left
        }) : 0)) + "px"
    }), fe.each({
        margin: "",
        padding: "",
        border: "Width"
    }, function(e, t) {
        fe.cssHooks[e + t] = {
            expand: function(n) {
                for (var i = 0, o = {}, s = "string" == typeof n ? n.split(" ") : [n]; i < 4; i++) o[e + He[i] + t] = s[i] || s[i - 2] || s[0];
                return o
            }
        }, ut.test(e) || (fe.cssHooks[e + t].set = U)
    }), fe.fn.extend({
        css: function(e, t) {
            return Re(this, function(e, t, n) {
                var i, o, s = {},
                    r = 0;
                if (fe.isArray(t)) {
                    for (i = pt(e), o = t.length; r < o; r++) s[t[r]] = fe.css(e, t[r], !1, i);
                    return s
                }
                return void 0 !== n ? fe.style(e, t, n) : fe.css(e, t)
            }, e, t, arguments.length > 1)
        },
        show: function() {
            return $(this, !0)
        },
        hide: function() {
            return $(this)
        },
        toggle: function(e) {
            return "boolean" == typeof e ? e ? this.show() : this.hide() : this.each(function() {
                Fe(this) ? fe(this).show() : fe(this).hide()
            })
        }
    }), fe.Tween = H, H.prototype = {
        constructor: H,
        init: function(e, t, n, i, o, s) {
            this.elem = e, this.prop = n, this.easing = o || fe.easing._default, this.options = t, this.start = this.now = this.cur(), this.end = i, this.unit = s || (fe.cssNumber[n] ? "" : "px")
        },
        cur: function() {
            var e = H.propHooks[this.prop];
            return e && e.get ? e.get(this) : H.propHooks._default.get(this)
        },
        run: function(e) {
            var t, n = H.propHooks[this.prop];
            return this.options.duration ? this.pos = t = fe.easing[this.easing](e, this.options.duration * e, 0, 1, this.options.duration) : this.pos = t = e, this.now = (this.end - this.start) * t + this.start, this.options.step && this.options.step.call(this.elem, this.now, this), n && n.set ? n.set(this) : H.propHooks._default.set(this), this
        }
    }, H.prototype.init.prototype = H.prototype, H.propHooks = {
        _default: {
            get: function(e) {
                var t;
                return 1 !== e.elem.nodeType || null != e.elem[e.prop] && null == e.elem.style[e.prop] ? e.elem[e.prop] : (t = fe.css(e.elem, e.prop, ""), t && "auto" !== t ? t : 0)
            },
            set: function(e) {
                fe.fx.step[e.prop] ? fe.fx.step[e.prop](e) : 1 !== e.elem.nodeType || null == e.elem.style[fe.cssProps[e.prop]] && !fe.cssHooks[e.prop] ? e.elem[e.prop] = e.now : fe.style(e.elem, e.prop, e.now + e.unit)
            }
        }
    }, H.propHooks.scrollTop = H.propHooks.scrollLeft = {
        set: function(e) {
            e.elem.nodeType && e.elem.parentNode && (e.elem[e.prop] = e.now)
        }
    }, fe.easing = {
        linear: function(e) {
            return e
        },
        swing: function(e) {
            return .5 - Math.cos(e * Math.PI) / 2
        },
        _default: "swing"
    }, fe.fx = H.prototype.init, fe.fx.step = {};
    var kt, St, Et = /^(?:toggle|show|hide)$/,
        Mt = /queueHooks$/;
    fe.Animation = fe.extend(B, {
            tweeners: {
                "*": [
                    function(e, t) {
                        var n = this.createTween(e, t);
                        return h(n.elem, e, Le.exec(t), n), n
                    }
                ]
            },
            tweener: function(e, t) {
                fe.isFunction(e) ? (t = e, e = ["*"]) : e = e.match(Ne);
                for (var n, i = 0, o = e.length; i < o; i++) n = e[i], B.tweeners[n] = B.tweeners[n] || [], B.tweeners[n].unshift(t)
            },
            prefilters: [_],
            prefilter: function(e, t) {
                t ? B.prefilters.unshift(e) : B.prefilters.push(e)
            }
        }), fe.speed = function(e, t, n) {
            var i = e && "object" == typeof e ? fe.extend({}, e) : {
                complete: n || !n && t || fe.isFunction(e) && e,
                duration: e,
                easing: n && t || t && !fe.isFunction(t) && t
            };
            return i.duration = fe.fx.off ? 0 : "number" == typeof i.duration ? i.duration : i.duration in fe.fx.speeds ? fe.fx.speeds[i.duration] : fe.fx.speeds._default, null != i.queue && i.queue !== !0 || (i.queue = "fx"), i.old = i.complete, i.complete = function() {
                fe.isFunction(i.old) && i.old.call(this), i.queue && fe.dequeue(this, i.queue)
            }, i
        }, fe.fn.extend({
            fadeTo: function(e, t, n, i) {
                return this.filter(Fe).css("opacity", 0).show().end().animate({
                    opacity: t
                }, e, n, i)
            },
            animate: function(e, t, n, i) {
                var o = fe.isEmptyObject(e),
                    s = fe.speed(t, n, i),
                    r = function() {
                        var t = B(this, fe.extend({}, e), s);
                        (o || fe._data(this, "finish")) && t.stop(!0)
                    };
                return r.finish = r, o || s.queue === !1 ? this.each(r) : this.queue(s.queue, r)
            },
            stop: function(e, t, n) {
                var i = function(e) {
                    var t = e.stop;
                    delete e.stop, t(n)
                };
                return "string" != typeof e && (n = t, t = e, e = void 0), t && e !== !1 && this.queue(e || "fx", []), this.each(function() {
                    var t = !0,
                        o = null != e && e + "queueHooks",
                        s = fe.timers,
                        r = fe._data(this);
                    if (o) r[o] && r[o].stop && i(r[o]);
                    else
                        for (o in r) r[o] && r[o].stop && Mt.test(o) && i(r[o]);
                    for (o = s.length; o--;) s[o].elem !== this || null != e && s[o].queue !== e || (s[o].anim.stop(n), t = !1, s.splice(o, 1));
                    !t && n || fe.dequeue(this, e)
                })
            },
            finish: function(e) {
                return e !== !1 && (e = e || "fx"), this.each(function() {
                    var t, n = fe._data(this),
                        i = n[e + "queue"],
                        o = n[e + "queueHooks"],
                        s = fe.timers,
                        r = i ? i.length : 0;
                    for (n.finish = !0, fe.queue(this, e, []), o && o.stop && o.stop.call(this, !0), t = s.length; t--;) s[t].elem === this && s[t].queue === e && (s[t].anim.stop(!0), s.splice(t, 1));
                    for (t = 0; t < r; t++) i[t] && i[t].finish && i[t].finish.call(this);
                    delete n.finish
                })
            }
        }), fe.each(["toggle", "show", "hide"], function(e, t) {
            var n = fe.fn[t];
            fe.fn[t] = function(e, i, o) {
                return null == e || "boolean" == typeof e ? n.apply(this, arguments) : this.animate(R(t, !0), e, i, o)
            }
        }), fe.each({
            slideDown: R("show"),
            slideUp: R("hide"),
            slideToggle: R("toggle"),
            fadeIn: {
                opacity: "show"
            },
            fadeOut: {
                opacity: "hide"
            },
            fadeToggle: {
                opacity: "toggle"
            }
        }, function(e, t) {
            fe.fn[e] = function(e, n, i) {
                return this.animate(t, e, n, i)
            }
        }), fe.timers = [], fe.fx.tick = function() {
            var e, t = fe.timers,
                n = 0;
            for (kt = fe.now(); n < t.length; n++) e = t[n], e() || t[n] !== e || t.splice(n--, 1);
            t.length || fe.fx.stop(), kt = void 0
        }, fe.fx.timer = function(e) {
            fe.timers.push(e), e() ? fe.fx.start() : fe.timers.pop()
        }, fe.fx.interval = 13, fe.fx.start = function() {
            St || (St = e.setInterval(fe.fx.tick, fe.fx.interval))
        }, fe.fx.stop = function() {
            e.clearInterval(St), St = null
        }, fe.fx.speeds = {
            slow: 600,
            fast: 200,
            _default: 400
        }, fe.fn.delay = function(t, n) {
            return t = fe.fx ? fe.fx.speeds[t] || t : t, n = n || "fx", this.queue(n, function(n, i) {
                var o = e.setTimeout(n, t);
                i.stop = function() {
                    e.clearTimeout(o)
                }
            })
        },
        function() {
            var e, t = ie.createElement("input"),
                n = ie.createElement("div"),
                i = ie.createElement("select"),
                o = i.appendChild(ie.createElement("option"));
            n = ie.createElement("div"), n.setAttribute("className", "t"), n.innerHTML = "  <link/><table></table><a href='/a'>a</a><input type='checkbox'/>", e = n.getElementsByTagName("a")[0], t.setAttribute("type", "checkbox"), n.appendChild(t), e = n.getElementsByTagName("a")[0], e.style.cssText = "top:1px", de.getSetAttribute = "t" !== n.className, de.style = /top/.test(e.getAttribute("style")), de.hrefNormalized = "/a" === e.getAttribute("href"), de.checkOn = !!t.value, de.optSelected = o.selected, de.enctype = !!ie.createElement("form").enctype, i.disabled = !0, de.optDisabled = !o.disabled, t = ie.createElement("input"), t.setAttribute("value", ""), de.input = "" === t.getAttribute("value"), t.value = "t", t.setAttribute("type", "radio"), de.radioValue = "t" === t.value
        }();
    var Nt = /\r/g,
        At = /[\x20\t\r\n\f]+/g;
    fe.fn.extend({
        val: function(e) {
            var t, n, i, o = this[0]; {
                if (arguments.length) return i = fe.isFunction(e), this.each(function(n) {
                    var o;
                    1 === this.nodeType && (o = i ? e.call(this, n, fe(this).val()) : e, null == o ? o = "" : "number" == typeof o ? o += "" : fe.isArray(o) && (o = fe.map(o, function(e) {
                        return null == e ? "" : e + ""
                    })), t = fe.valHooks[this.type] || fe.valHooks[this.nodeName.toLowerCase()], t && "set" in t && void 0 !== t.set(this, o, "value") || (this.value = o))
                });
                if (o) return t = fe.valHooks[o.type] || fe.valHooks[o.nodeName.toLowerCase()], t && "get" in t && void 0 !== (n = t.get(o, "value")) ? n : (n = o.value, "string" == typeof n ? n.replace(Nt, "") : null == n ? "" : n)
            }
        }
    }), fe.extend({
        valHooks: {
            option: {
                get: function(e) {
                    var t = fe.find.attr(e, "value");
                    return null != t ? t : fe.trim(fe.text(e)).replace(At, " ")
                }
            },
            select: {
                get: function(e) {
                    for (var t, n, i = e.options, o = e.selectedIndex, s = "select-one" === e.type || o < 0, r = s ? null : [], a = s ? o + 1 : i.length, l = o < 0 ? a : s ? o : 0; l < a; l++)
                        if (n = i[l], (n.selected || l === o) && (de.optDisabled ? !n.disabled : null === n.getAttribute("disabled")) && (!n.parentNode.disabled || !fe.nodeName(n.parentNode, "optgroup"))) {
                            if (t = fe(n).val(), s) return t;
                            r.push(t)
                        }
                    return r
                },
                set: function(e, t) {
                    for (var n, i, o = e.options, s = fe.makeArray(t), r = o.length; r--;)
                        if (i = o[r], fe.inArray(fe.valHooks.option.get(i), s) > -1) try {
                            i.selected = n = !0
                        } catch (a) {
                            i.scrollHeight
                        } else i.selected = !1;
                    return n || (e.selectedIndex = -1), o
                }
            }
        }
    }), fe.each(["radio", "checkbox"], function() {
        fe.valHooks[this] = {
            set: function(e, t) {
                if (fe.isArray(t)) return e.checked = fe.inArray(fe(e).val(), t) > -1
            }
        }, de.checkOn || (fe.valHooks[this].get = function(e) {
            return null === e.getAttribute("value") ? "on" : e.value
        })
    });
    var It, Ot, $t = fe.expr.attrHandle,
        Ut = /^(?:checked|selected)$/i,
        jt = de.getSetAttribute,
        Lt = de.input;
    fe.fn.extend({
        attr: function(e, t) {
            return Re(this, fe.attr, e, t, arguments.length > 1)
        },
        removeAttr: function(e) {
            return this.each(function() {
                fe.removeAttr(this, e)
            })
        }
    }), fe.extend({
        attr: function(e, t, n) {
            var i, o, s = e.nodeType;
            if (3 !== s && 8 !== s && 2 !== s) return "undefined" == typeof e.getAttribute ? fe.prop(e, t, n) : (1 === s && fe.isXMLDoc(e) || (t = t.toLowerCase(), o = fe.attrHooks[t] || (fe.expr.match.bool.test(t) ? Ot : It)), void 0 !== n ? null === n ? void fe.removeAttr(e, t) : o && "set" in o && void 0 !== (i = o.set(e, n, t)) ? i : (e.setAttribute(t, n + ""), n) : o && "get" in o && null !== (i = o.get(e, t)) ? i : (i = fe.find.attr(e, t), null == i ? void 0 : i))
        },
        attrHooks: {
            type: {
                set: function(e, t) {
                    if (!de.radioValue && "radio" === t && fe.nodeName(e, "input")) {
                        var n = e.value;
                        return e.setAttribute("type", t), n && (e.value = n), t
                    }
                }
            }
        },
        removeAttr: function(e, t) {
            var n, i, o = 0,
                s = t && t.match(Ne);
            if (s && 1 === e.nodeType)
                for (; n = s[o++];) i = fe.propFix[n] || n, fe.expr.match.bool.test(n) ? Lt && jt || !Ut.test(n) ? e[i] = !1 : e[fe.camelCase("default-" + n)] = e[i] = !1 : fe.attr(e, n, ""), e.removeAttribute(jt ? n : i)
        }
    }), Ot = {
        set: function(e, t, n) {
            return t === !1 ? fe.removeAttr(e, n) : Lt && jt || !Ut.test(n) ? e.setAttribute(!jt && fe.propFix[n] || n, n) : e[fe.camelCase("default-" + n)] = e[n] = !0, n
        }
    }, fe.each(fe.expr.match.bool.source.match(/\w+/g), function(e, t) {
        var n = $t[t] || fe.find.attr;
        Lt && jt || !Ut.test(t) ? $t[t] = function(e, t, i) {
            var o, s;
            return i || (s = $t[t], $t[t] = o, o = null != n(e, t, i) ? t.toLowerCase() : null, $t[t] = s), o
        } : $t[t] = function(e, t, n) {
            if (!n) return e[fe.camelCase("default-" + t)] ? t.toLowerCase() : null
        }
    }), Lt && jt || (fe.attrHooks.value = {
        set: function(e, t, n) {
            return fe.nodeName(e, "input") ? void(e.defaultValue = t) : It && It.set(e, t, n)
        }
    }), jt || (It = {
        set: function(e, t, n) {
            var i = e.getAttributeNode(n);
            if (i || e.setAttributeNode(i = e.ownerDocument.createAttribute(n)), i.value = t += "", "value" === n || t === e.getAttribute(n)) return t
        }
    }, $t.id = $t.name = $t.coords = function(e, t, n) {
        var i;
        if (!n) return (i = e.getAttributeNode(t)) && "" !== i.value ? i.value : null
    }, fe.valHooks.button = {
        get: function(e, t) {
            var n = e.getAttributeNode(t);
            if (n && n.specified) return n.value
        },
        set: It.set
    }, fe.attrHooks.contenteditable = {
        set: function(e, t, n) {
            It.set(e, "" !== t && t, n)
        }
    }, fe.each(["width", "height"], function(e, t) {
        fe.attrHooks[t] = {
            set: function(e, n) {
                if ("" === n) return e.setAttribute(t, "auto"), n
            }
        }
    })), de.style || (fe.attrHooks.style = {
        get: function(e) {
            return e.style.cssText || void 0
        },
        set: function(e, t) {
            return e.style.cssText = t + ""
        }
    });
    var Ht = /^(?:input|select|textarea|button|object)$/i,
        Ft = /^(?:a|area)$/i;
    fe.fn.extend({
        prop: function(e, t) {
            return Re(this, fe.prop, e, t, arguments.length > 1)
        },
        removeProp: function(e) {
            return e = fe.propFix[e] || e, this.each(function() {
                try {
                    this[e] = void 0, delete this[e]
                } catch (t) {}
            })
        }
    }), fe.extend({
        prop: function(e, t, n) {
            var i, o, s = e.nodeType;
            if (3 !== s && 8 !== s && 2 !== s) return 1 === s && fe.isXMLDoc(e) || (t = fe.propFix[t] || t, o = fe.propHooks[t]), void 0 !== n ? o && "set" in o && void 0 !== (i = o.set(e, n, t)) ? i : e[t] = n : o && "get" in o && null !== (i = o.get(e, t)) ? i : e[t]
        },
        propHooks: {
            tabIndex: {
                get: function(e) {
                    var t = fe.find.attr(e, "tabindex");
                    return t ? parseInt(t, 10) : Ht.test(e.nodeName) || Ft.test(e.nodeName) && e.href ? 0 : -1
                }
            }
        },
        propFix: {
            "for": "htmlFor",
            "class": "className"
        }
    }), de.hrefNormalized || fe.each(["href", "src"], function(e, t) {
        fe.propHooks[t] = {
            get: function(e) {
                return e.getAttribute(t, 4)
            }
        }
    }), de.optSelected || (fe.propHooks.selected = {
        get: function(e) {
            var t = e.parentNode;
            return t && (t.selectedIndex, t.parentNode && t.parentNode.selectedIndex), null
        },
        set: function(e) {
            var t = e.parentNode;
            t && (t.selectedIndex, t.parentNode && t.parentNode.selectedIndex)
        }
    }), fe.each(["tabIndex", "readOnly", "maxLength", "cellSpacing", "cellPadding", "rowSpan", "colSpan", "useMap", "frameBorder", "contentEditable"], function() {
        fe.propFix[this.toLowerCase()] = this
    }), de.enctype || (fe.propFix.enctype = "encoding");
    var Rt = /[\t\r\n\f]/g;
    fe.fn.extend({
        addClass: function(e) {
            var t, n, i, o, s, r, a, l = 0;
            if (fe.isFunction(e)) return this.each(function(t) {
                fe(this).addClass(e.call(this, t, V(this)))
            });
            if ("string" == typeof e && e)
                for (t = e.match(Ne) || []; n = this[l++];)
                    if (o = V(n), i = 1 === n.nodeType && (" " + o + " ").replace(Rt, " ")) {
                        for (r = 0; s = t[r++];) i.indexOf(" " + s + " ") < 0 && (i += s + " ");
                        a = fe.trim(i), o !== a && fe.attr(n, "class", a)
                    }
            return this
        },
        removeClass: function(e) {
            var t, n, i, o, s, r, a, l = 0;
            if (fe.isFunction(e)) return this.each(function(t) {
                fe(this).removeClass(e.call(this, t, V(this)))
            });
            if (!arguments.length) return this.attr("class", "");
            if ("string" == typeof e && e)
                for (t = e.match(Ne) || []; n = this[l++];)
                    if (o = V(n), i = 1 === n.nodeType && (" " + o + " ").replace(Rt, " ")) {
                        for (r = 0; s = t[r++];)
                            for (; i.indexOf(" " + s + " ") > -1;) i = i.replace(" " + s + " ", " ");
                        a = fe.trim(i), o !== a && fe.attr(n, "class", a)
                    }
            return this
        },
        toggleClass: function(e, t) {
            var n = typeof e;
            return "boolean" == typeof t && "string" === n ? t ? this.addClass(e) : this.removeClass(e) : fe.isFunction(e) ? this.each(function(n) {
                fe(this).toggleClass(e.call(this, n, V(this), t), t)
            }) : this.each(function() {
                var t, i, o, s;
                if ("string" === n)
                    for (i = 0, o = fe(this), s = e.match(Ne) || []; t = s[i++];) o.hasClass(t) ? o.removeClass(t) : o.addClass(t);
                else void 0 !== e && "boolean" !== n || (t = V(this), t && fe._data(this, "__className__", t), fe.attr(this, "class", t || e === !1 ? "" : fe._data(this, "__className__") || ""))
            })
        },
        hasClass: function(e) {
            var t, n, i = 0;
            for (t = " " + e + " "; n = this[i++];)
                if (1 === n.nodeType && (" " + V(n) + " ").replace(Rt, " ").indexOf(t) > -1) return !0;
            return !1
        }
    }), fe.each("blur focus focusin focusout load resize scroll unload click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave change select submit keydown keypress keyup error contextmenu".split(" "), function(e, t) {
        fe.fn[t] = function(e, n) {
            return arguments.length > 0 ? this.on(t, null, e, n) : this.trigger(t)
        }
    }), fe.fn.extend({
        hover: function(e, t) {
            return this.mouseenter(e).mouseleave(t || e)
        }
    });
    var Pt = e.location,
        _t = fe.now(),
        qt = /\?/,
        Bt = /(,)|(\[|{)|(}|])|"(?:[^"\\\r\n]|\\["\\\/bfnrt]|\\u[\da-fA-F]{4})*"\s*:?|true|false|null|-?(?!0\d)\d+(?:\.\d+|)(?:[eE][+-]?\d+|)/g;
    fe.parseJSON = function(t) {
        if (e.JSON && e.JSON.parse) return e.JSON.parse(t + "");
        var n, i = null,
            o = fe.trim(t + "");
        return o && !fe.trim(o.replace(Bt, function(e, t, o, s) {
            return n && t && (i = 0), 0 === i ? e : (n = o || t, i += !s - !o, "")
        })) ? Function("return " + o)() : fe.error("Invalid JSON: " + t)
    }, fe.parseXML = function(t) {
        var n, i;
        if (!t || "string" != typeof t) return null;
        try {
            e.DOMParser ? (i = new e.DOMParser, n = i.parseFromString(t, "text/xml")) : (n = new e.ActiveXObject("Microsoft.XMLDOM"), n.async = "false", n.loadXML(t))
        } catch (o) {
            n = void 0
        }
        return n && n.documentElement && !n.getElementsByTagName("parsererror").length || fe.error("Invalid XML: " + t), n
    };
    var Vt = /#.*$/,
        Wt = /([?&])_=[^&]*/,
        zt = /^(.*?):[ \t]*([^\r\n]*)\r?$/gm,
        Yt = /^(?:about|app|app-storage|.+-extension|file|res|widget):$/,
        Qt = /^(?:GET|HEAD)$/,
        Xt = /^\/\//,
        Jt = /^([\w.+-]+:)(?:\/\/(?:[^\/?#]*@|)([^\/?#:]*)(?::(\d+)|)|)/,
        Gt = {},
        Kt = {},
        Zt = "*/".concat("*"),
        en = Pt.href,
        tn = Jt.exec(en.toLowerCase()) || [];
    fe.extend({
        active: 0,
        lastModified: {},
        etag: {},
        ajaxSettings: {
            url: en,
            type: "GET",
            isLocal: Yt.test(tn[1]),
            global: !0,
            processData: !0,
            async: !0,
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            accepts: {
                "*": Zt,
                text: "text/plain",
                html: "text/html",
                xml: "application/xml, text/xml",
                json: "application/json, text/javascript"
            },
            contents: {
                xml: /\bxml\b/,
                html: /\bhtml/,
                json: /\bjson\b/
            },
            responseFields: {
                xml: "responseXML",
                text: "responseText",
                json: "responseJSON"
            },
            converters: {
                "* text": String,
                "text html": !0,
                "text json": fe.parseJSON,
                "text xml": fe.parseXML
            },
            flatOptions: {
                url: !0,
                context: !0
            }
        },
        ajaxSetup: function(e, t) {
            return t ? Y(Y(e, fe.ajaxSettings), t) : Y(fe.ajaxSettings, e)
        },
        ajaxPrefilter: W(Gt),
        ajaxTransport: W(Kt),
        ajax: function(t, n) {
            function i(t, n, i, o) {
                var s, d, y, b, T, C = n;
                2 !== w && (w = 2, l && e.clearTimeout(l), u = void 0, a = o || "", x.readyState = t > 0 ? 4 : 0, s = t >= 200 && t < 300 || 304 === t, i && (b = Q(h, x, i)), b = X(h, b, x, s), s ? (h.ifModified && (T = x.getResponseHeader("Last-Modified"), T && (fe.lastModified[r] = T), T = x.getResponseHeader("etag"), T && (fe.etag[r] = T)), 204 === t || "HEAD" === h.type ? C = "nocontent" : 304 === t ? C = "notmodified" : (C = b.state, d = b.data, y = b.error, s = !y)) : (y = C, !t && C || (C = "error", t < 0 && (t = 0))), x.status = t, x.statusText = (n || C) + "", s ? m.resolveWith(f, [d, C, x]) : m.rejectWith(f, [x, C, y]), x.statusCode(v), v = void 0, c && p.trigger(s ? "ajaxSuccess" : "ajaxError", [x, h, s ? d : y]), g.fireWith(f, [x, C]), c && (p.trigger("ajaxComplete", [x, h]), --fe.active || fe.event.trigger("ajaxStop")))
            }
            "object" == typeof t && (n = t, t = void 0), n = n || {};
            var o, s, r, a, l, c, u, d, h = fe.ajaxSetup({}, n),
                f = h.context || h,
                p = h.context && (f.nodeType || f.jquery) ? fe(f) : fe.event,
                m = fe.Deferred(),
                g = fe.Callbacks("once memory"),
                v = h.statusCode || {},
                y = {},
                b = {},
                w = 0,
                T = "canceled",
                x = {
                    readyState: 0,
                    getResponseHeader: function(e) {
                        var t;
                        if (2 === w) {
                            if (!d)
                                for (d = {}; t = zt.exec(a);) d[t[1].toLowerCase()] = t[2];
                            t = d[e.toLowerCase()]
                        }
                        return null == t ? null : t
                    },
                    getAllResponseHeaders: function() {
                        return 2 === w ? a : null
                    },
                    setRequestHeader: function(e, t) {
                        var n = e.toLowerCase();
                        return w || (e = b[n] = b[n] || e, y[e] = t), this
                    },
                    overrideMimeType: function(e) {
                        return w || (h.mimeType = e), this
                    },
                    statusCode: function(e) {
                        var t;
                        if (e)
                            if (w < 2)
                                for (t in e) v[t] = [v[t], e[t]];
                            else x.always(e[x.status]);
                        return this
                    },
                    abort: function(e) {
                        var t = e || T;
                        return u && u.abort(t), i(0, t), this
                    }
                };
            if (m.promise(x).complete = g.add, x.success = x.done, x.error = x.fail, h.url = ((t || h.url || en) + "").replace(Vt, "").replace(Xt, tn[1] + "//"), h.type = n.method || n.type || h.method || h.type, h.dataTypes = fe.trim(h.dataType || "*").toLowerCase().match(Ne) || [""], null == h.crossDomain && (o = Jt.exec(h.url.toLowerCase()), h.crossDomain = !(!o || o[1] === tn[1] && o[2] === tn[2] && (o[3] || ("http:" === o[1] ? "80" : "443")) === (tn[3] || ("http:" === tn[1] ? "80" : "443")))), h.data && h.processData && "string" != typeof h.data && (h.data = fe.param(h.data, h.traditional)), z(Gt, h, n, x), 2 === w) return x;
            c = fe.event && h.global, c && 0 === fe.active++ && fe.event.trigger("ajaxStart"), h.type = h.type.toUpperCase(), h.hasContent = !Qt.test(h.type), r = h.url, h.hasContent || (h.data && (r = h.url += (qt.test(r) ? "&" : "?") + h.data, delete h.data), h.cache === !1 && (h.url = Wt.test(r) ? r.replace(Wt, "$1_=" + _t++) : r + (qt.test(r) ? "&" : "?") + "_=" + _t++)), h.ifModified && (fe.lastModified[r] && x.setRequestHeader("If-Modified-Since", fe.lastModified[r]), fe.etag[r] && x.setRequestHeader("If-None-Match", fe.etag[r])), (h.data && h.hasContent && h.contentType !== !1 || n.contentType) && x.setRequestHeader("Content-Type", h.contentType), x.setRequestHeader("Accept", h.dataTypes[0] && h.accepts[h.dataTypes[0]] ? h.accepts[h.dataTypes[0]] + ("*" !== h.dataTypes[0] ? ", " + Zt + "; q=0.01" : "") : h.accepts["*"]);
            for (s in h.headers) x.setRequestHeader(s, h.headers[s]);
            if (h.beforeSend && (h.beforeSend.call(f, x, h) === !1 || 2 === w)) return x.abort();
            T = "abort";
            for (s in {
                success: 1,
                error: 1,
                complete: 1
            }) x[s](h[s]);
            if (u = z(Kt, h, n, x)) {
                if (x.readyState = 1, c && p.trigger("ajaxSend", [x, h]), 2 === w) return x;
                h.async && h.timeout > 0 && (l = e.setTimeout(function() {
                    x.abort("timeout")
                }, h.timeout));
                try {
                    w = 1, u.send(y, i)
                } catch (C) {
                    if (!(w < 2)) throw C;
                    i(-1, C)
                }
            } else i(-1, "No Transport");
            return x
        },
        getJSON: function(e, t, n) {
            return fe.get(e, t, n, "json")
        },
        getScript: function(e, t) {
            return fe.get(e, void 0, t, "script")
        }
    }), fe.each(["get", "post"], function(e, t) {
        fe[t] = function(e, n, i, o) {
            return fe.isFunction(n) && (o = o || i, i = n, n = void 0), fe.ajax(fe.extend({
                url: e,
                type: t,
                dataType: o,
                data: n,
                success: i
            }, fe.isPlainObject(e) && e))
        }
    }), fe._evalUrl = function(e) {
        return fe.ajax({
            url: e,
            type: "GET",
            dataType: "script",
            cache: !0,
            async: !1,
            global: !1,
            "throws": !0
        })
    }, fe.fn.extend({
        wrapAll: function(e) {
            if (fe.isFunction(e)) return this.each(function(t) {
                fe(this).wrapAll(e.call(this, t))
            });
            if (this[0]) {
                var t = fe(e, this[0].ownerDocument).eq(0).clone(!0);
                this[0].parentNode && t.insertBefore(this[0]), t.map(function() {
                    for (var e = this; e.firstChild && 1 === e.firstChild.nodeType;) e = e.firstChild;
                    return e
                }).append(this)
            }
            return this
        },
        wrapInner: function(e) {
            return fe.isFunction(e) ? this.each(function(t) {
                fe(this).wrapInner(e.call(this, t))
            }) : this.each(function() {
                var t = fe(this),
                    n = t.contents();
                n.length ? n.wrapAll(e) : t.append(e)
            })
        },
        wrap: function(e) {
            var t = fe.isFunction(e);
            return this.each(function(n) {
                fe(this).wrapAll(t ? e.call(this, n) : e)
            })
        },
        unwrap: function() {
            return this.parent().each(function() {
                fe.nodeName(this, "body") || fe(this).replaceWith(this.childNodes)
            }).end()
        }
    }), fe.expr.filters.hidden = function(e) {
        return de.reliableHiddenOffsets() ? e.offsetWidth <= 0 && e.offsetHeight <= 0 && !e.getClientRects().length : G(e)
    }, fe.expr.filters.visible = function(e) {
        return !fe.expr.filters.hidden(e)
    };
    var nn = /%20/g,
        on = /\[\]$/,
        sn = /\r?\n/g,
        rn = /^(?:submit|button|image|reset|file)$/i,
        an = /^(?:input|select|textarea|keygen)/i;
    fe.param = function(e, t) {
        var n, i = [],
            o = function(e, t) {
                t = fe.isFunction(t) ? t() : null == t ? "" : t, i[i.length] = encodeURIComponent(e) + "=" + encodeURIComponent(t)
            };
        if (void 0 === t && (t = fe.ajaxSettings && fe.ajaxSettings.traditional), fe.isArray(e) || e.jquery && !fe.isPlainObject(e)) fe.each(e, function() {
            o(this.name, this.value)
        });
        else
            for (n in e) K(n, e[n], t, o);
        return i.join("&").replace(nn, "+")
    }, fe.fn.extend({
        serialize: function() {
            return fe.param(this.serializeArray())
        },
        serializeArray: function() {
            return this.map(function() {
                var e = fe.prop(this, "elements");
                return e ? fe.makeArray(e) : this
            }).filter(function() {
                var e = this.type;
                return this.name && !fe(this).is(":disabled") && an.test(this.nodeName) && !rn.test(e) && (this.checked || !Pe.test(e))
            }).map(function(e, t) {
                var n = fe(this).val();
                return null == n ? null : fe.isArray(n) ? fe.map(n, function(e) {
                    return {
                        name: t.name,
                        value: e.replace(sn, "\r\n")
                    }
                }) : {
                    name: t.name,
                    value: n.replace(sn, "\r\n")
                }
            }).get()
        }
    }), fe.ajaxSettings.xhr = void 0 !== e.ActiveXObject ? function() {
        return this.isLocal ? ee() : ie.documentMode > 8 ? Z() : /^(get|post|head|put|delete|options)$/i.test(this.type) && Z() || ee()
    } : Z;
    var ln = 0,
        cn = {},
        un = fe.ajaxSettings.xhr();
    e.attachEvent && e.attachEvent("onunload", function() {
        for (var e in cn) cn[e](void 0, !0)
    }), de.cors = !!un && "withCredentials" in un, un = de.ajax = !!un, un && fe.ajaxTransport(function(t) {
        if (!t.crossDomain || de.cors) {
            var n;
            return {
                send: function(i, o) {
                    var s, r = t.xhr(),
                        a = ++ln;
                    if (r.open(t.type, t.url, t.async, t.username, t.password), t.xhrFields)
                        for (s in t.xhrFields) r[s] = t.xhrFields[s];
                    t.mimeType && r.overrideMimeType && r.overrideMimeType(t.mimeType), t.crossDomain || i["X-Requested-With"] || (i["X-Requested-With"] = "XMLHttpRequest");
                    for (s in i) void 0 !== i[s] && r.setRequestHeader(s, i[s] + "");
                    r.send(t.hasContent && t.data || null), n = function(e, i) {
                        var s, l, c;
                        if (n && (i || 4 === r.readyState))
                            if (delete cn[a], n = void 0, r.onreadystatechange = fe.noop, i) 4 !== r.readyState && r.abort();
                            else {
                                c = {}, s = r.status, "string" == typeof r.responseText && (c.text = r.responseText);
                                try {
                                    l = r.statusText
                                } catch (u) {
                                    l = ""
                                }
                                s || !t.isLocal || t.crossDomain ? 1223 === s && (s = 204) : s = c.text ? 200 : 404
                            }
                        c && o(s, l, c, r.getAllResponseHeaders())
                    }, 
                    t.async ? 4 === r.readyState ? e.setTimeout(n) : r.onreadystatechange = cn[a] = n : n()
                },
                abort: function() {
                    n && n(void 0, !0)
                }
            }
        }
    }), fe.ajaxSetup({
        accepts: {
            script: "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"
        },
        contents: {
            script: /\b(?:java|ecma)script\b/
        },
        converters: {
            "text script": function(e) {
                return fe.globalEval(e), e
            }
        }
    }), fe.ajaxPrefilter("script", function(e) {
        void 0 === e.cache && (e.cache = !1), e.crossDomain && (e.type = "GET", e.global = !1)
    }), fe.ajaxTransport("script", function(e) {
        if (e.crossDomain) {
            var t, n = ie.head || fe("head")[0] || ie.documentElement;
            return {
                send: function(i, o) {
                    t = ie.createElement("script"), t.async = !0, e.scriptCharset && (t.charset = e.scriptCharset), t.src = e.url, t.onload = t.onreadystatechange = function(e, n) {
                        (n || !t.readyState || /loaded|complete/.test(t.readyState)) && (t.onload = t.onreadystatechange = null, t.parentNode && t.parentNode.removeChild(t), t = null, n || o(200, "success"))
                    }, n.insertBefore(t, n.firstChild)
                },
                abort: function() {
                    t && t.onload(void 0, !0)
                }
            }
        }
    });
    var dn = [],
        hn = /(=)\?(?=&|$)|\?\?/;
    fe.ajaxSetup({
        jsonp: "callback",
        jsonpCallback: function() {
            var e = dn.pop() || fe.expando + "_" + _t++;
            return this[e] = !0, e
        }
    }), fe.ajaxPrefilter("json jsonp", function(t, n, i) {
        var o, s, r, a = t.jsonp !== !1 && (hn.test(t.url) ? "url" : "string" == typeof t.data && 0 === (t.contentType || "").indexOf("application/x-www-form-urlencoded") && hn.test(t.data) && "data");
        if (a || "jsonp" === t.dataTypes[0]) return o = t.jsonpCallback = fe.isFunction(t.jsonpCallback) ? t.jsonpCallback() : t.jsonpCallback, a ? t[a] = t[a].replace(hn, "$1" + o) : t.jsonp !== !1 && (t.url += (qt.test(t.url) ? "&" : "?") + t.jsonp + "=" + o), t.converters["script json"] = function() {
            return r || fe.error(o + " was not called"), r[0]
        }, t.dataTypes[0] = "json", s = e[o], e[o] = function() {
            r = arguments
        }, i.always(function() {
            void 0 === s ? fe(e).removeProp(o) : e[o] = s, t[o] && (t.jsonpCallback = n.jsonpCallback, dn.push(o)), r && fe.isFunction(s) && s(r[0]), r = s = void 0
        }), "script"
    }), fe.parseHTML = function(e, t, n) {
        if (!e || "string" != typeof e) return null;
        "boolean" == typeof t && (n = t, t = !1), t = t || ie;
        var i = xe.exec(e),
            o = !n && [];
        return i ? [t.createElement(i[1])] : (i = v([e], t, o), o && o.length && fe(o).remove(), fe.merge([], i.childNodes))
    };
    var fn = fe.fn.load;
    fe.fn.load = function(e, t, n) {
        if ("string" != typeof e && fn) return fn.apply(this, arguments);
        var i, o, s, r = this,
            a = e.indexOf(" ");
        return a > -1 && (i = fe.trim(e.slice(a, e.length)), e = e.slice(0, a)), fe.isFunction(t) ? (n = t, t = void 0) : t && "object" == typeof t && (o = "POST"), r.length > 0 && fe.ajax({
            url: e,
            type: o || "GET",
            dataType: "html",
            data: t
        }).done(function(e) {
            s = arguments, r.html(i ? fe("<div>").append(fe.parseHTML(e)).find(i) : e)
        }).always(n && function(e, t) {
            r.each(function() {
                n.apply(this, s || [e.responseText, t, e])
            })
        }), this
    }, fe.each(["ajaxStart", "ajaxStop", "ajaxComplete", "ajaxError", "ajaxSuccess", "ajaxSend"], function(e, t) {
        fe.fn[t] = function(e) {
            return this.on(t, e)
        }
    }), fe.expr.filters.animated = function(e) {
        return fe.grep(fe.timers, function(t) {
            return e === t.elem
        }).length
    }, fe.offset = {
        setOffset: function(e, t, n) {
            var i, o, s, r, a, l, c, u = fe.css(e, "position"),
                d = fe(e),
                h = {};
            "static" === u && (e.style.position = "relative"), a = d.offset(), s = fe.css(e, "top"), l = fe.css(e, "left"), c = ("absolute" === u || "fixed" === u) && fe.inArray("auto", [s, l]) > -1, c ? (i = d.position(), r = i.top, o = i.left) : (r = parseFloat(s) || 0, o = parseFloat(l) || 0), fe.isFunction(t) && (t = t.call(e, n, fe.extend({}, a))), null != t.top && (h.top = t.top - a.top + r), null != t.left && (h.left = t.left - a.left + o), "using" in t ? t.using.call(e, h) : d.css(h)
        }
    }, fe.fn.extend({
        offset: function(e) {
            if (arguments.length) return void 0 === e ? this : this.each(function(t) {
                fe.offset.setOffset(this, e, t)
            });
            var t, n, i = {
                    top: 0,
                    left: 0
                },
                o = this[0],
                s = o && o.ownerDocument;
            if (s) return t = s.documentElement, fe.contains(t, o) ? ("undefined" != typeof o.getBoundingClientRect && (i = o.getBoundingClientRect()), n = te(s), {
                top: i.top + (n.pageYOffset || t.scrollTop) - (t.clientTop || 0),
                left: i.left + (n.pageXOffset || t.scrollLeft) - (t.clientLeft || 0)
            }) : i
        },
        position: function() {
            if (this[0]) {
                var e, t, n = {
                        top: 0,
                        left: 0
                    },
                    i = this[0];
                return "fixed" === fe.css(i, "position") ? t = i.getBoundingClientRect() : (e = this.offsetParent(), t = this.offset(), fe.nodeName(e[0], "html") || (n = e.offset()), n.top += fe.css(e[0], "borderTopWidth", !0), n.left += fe.css(e[0], "borderLeftWidth", !0)), {
                    top: t.top - n.top - fe.css(i, "marginTop", !0),
                    left: t.left - n.left - fe.css(i, "marginLeft", !0)
                }
            }
        },
        offsetParent: function() {
            return this.map(function() {
                for (var e = this.offsetParent; e && !fe.nodeName(e, "html") && "static" === fe.css(e, "position");) e = e.offsetParent;
                return e || ft
            })
        }
    }), fe.each({
        scrollLeft: "pageXOffset",
        scrollTop: "pageYOffset"
    }, function(e, t) {
        var n = /Y/.test(t);
        fe.fn[e] = function(i) {
            return Re(this, function(e, i, o) {
                var s = te(e);
                return void 0 === o ? s ? t in s ? s[t] : s.document.documentElement[i] : e[i] : void(s ? s.scrollTo(n ? fe(s).scrollLeft() : o, n ? o : fe(s).scrollTop()) : e[i] = o)
            }, e, i, arguments.length, null)
        }
    }), fe.each(["top", "left"], function(e, t) {
        fe.cssHooks[t] = I(de.pixelPosition, function(e, n) {
            if (n) return n = mt(e, t), dt.test(n) ? fe(e).position()[t] + "px" : n
        })
    }), fe.each({
        Height: "height",
        Width: "width"
    }, function(e, t) {
        fe.each({
            padding: "inner" + e,
            content: t,
            "": "outer" + e
        }, function(n, i) {
            fe.fn[i] = function(i, o) {
                var s = arguments.length && (n || "boolean" != typeof i),
                    r = n || (i === !0 || o === !0 ? "margin" : "border");
                return Re(this, function(t, n, i) {
                    var o;
                    return fe.isWindow(t) ? t.document.documentElement["client" + e] : 9 === t.nodeType ? (o = t.documentElement, Math.max(t.body["scroll" + e], o["scroll" + e], t.body["offset" + e], o["offset" + e], o["client" + e])) : void 0 === i ? fe.css(t, n, r) : fe.style(t, n, i, r)
                }, t, s ? i : void 0, s, null)
            }
        })
    }), fe.fn.extend({
        bind: function(e, t, n) {
            return this.on(e, null, t, n)
        },
        unbind: function(e, t) {
            return this.off(e, null, t)
        },
        delegate: function(e, t, n, i) {
            return this.on(t, e, n, i)
        },
        undelegate: function(e, t, n) {
            return 1 === arguments.length ? this.off(e, "**") : this.off(t, e || "**", n)
        }
    }), fe.fn.size = function() {
        return this.length
    }, fe.fn.andSelf = fe.fn.addBack, "function" == typeof define && define.amd && define("jquery", [], function() {
        return fe
    });
    var pn = e.jQuery,
        mn = e.$;
    return fe.noConflict = function(t) {
        return e.$ === fe && (e.$ = mn), t && e.jQuery === fe && (e.jQuery = pn), fe
    }, t || (e.jQuery = e.$ = fe), fe
}), jQuery.easing.jswing = jQuery.easing.swing, jQuery.extend(jQuery.easing, {
    def: "easeOutQuad",
    swing: function(e, t, n, i, o) {
        return jQuery.easing[jQuery.easing.def](e, t, n, i, o)
    },
    easeInQuad: function(e, t, n, i, o) {
        return i * (t /= o) * t + n
    },
    easeOutQuad: function(e, t, n, i, o) {
        return -i * (t /= o) * (t - 2) + n
    },
    easeInOutQuad: function(e, t, n, i, o) {
        return (t /= o / 2) < 1 ? i / 2 * t * t + n : -i / 2 * (--t * (t - 2) - 1) + n
    },
    easeInCubic: function(e, t, n, i, o) {
        return i * (t /= o) * t * t + n
    },
    easeOutCubic: function(e, t, n, i, o) {
        return i * ((t = t / o - 1) * t * t + 1) + n
    },
    easeInOutCubic: function(e, t, n, i, o) {
        return (t /= o / 2) < 1 ? i / 2 * t * t * t + n : i / 2 * ((t -= 2) * t * t + 2) + n
    },
    easeInQuart: function(e, t, n, i, o) {
        return i * (t /= o) * t * t * t + n
    },
    easeOutQuart: function(e, t, n, i, o) {
        return -i * ((t = t / o - 1) * t * t * t - 1) + n
    },
    easeInOutQuart: function(e, t, n, i, o) {
        return (t /= o / 2) < 1 ? i / 2 * t * t * t * t + n : -i / 2 * ((t -= 2) * t * t * t - 2) + n
    },
    easeInQuint: function(e, t, n, i, o) {
        return i * (t /= o) * t * t * t * t + n
    },
    easeOutQuint: function(e, t, n, i, o) {
        return i * ((t = t / o - 1) * t * t * t * t + 1) + n
    },
    easeInOutQuint: function(e, t, n, i, o) {
        return (t /= o / 2) < 1 ? i / 2 * t * t * t * t * t + n : i / 2 * ((t -= 2) * t * t * t * t + 2) + n
    },
    easeInSine: function(e, t, n, i, o) {
        return -i * Math.cos(t / o * (Math.PI / 2)) + i + n
    },
    easeOutSine: function(e, t, n, i, o) {
        return i * Math.sin(t / o * (Math.PI / 2)) + n
    },
    easeInOutSine: function(e, t, n, i, o) {
        return -i / 2 * (Math.cos(Math.PI * t / o) - 1) + n
    },
    easeInExpo: function(e, t, n, i, o) {
        return 0 == t ? n : i * Math.pow(2, 10 * (t / o - 1)) + n
    },
    easeOutExpo: function(e, t, n, i, o) {
        return t == o ? n + i : i * (-Math.pow(2, -10 * t / o) + 1) + n
    },
    easeInOutExpo: function(e, t, n, i, o) {
        return 0 == t ? n : t == o ? n + i : (t /= o / 2) < 1 ? i / 2 * Math.pow(2, 10 * (t - 1)) + n : i / 2 * (-Math.pow(2, -10 * --t) + 2) + n
    },
    easeInCirc: function(e, t, n, i, o) {
        return -i * (Math.sqrt(1 - (t /= o) * t) - 1) + n
    },
    easeOutCirc: function(e, t, n, i, o) {
        return i * Math.sqrt(1 - (t = t / o - 1) * t) + n
    },
    easeInOutCirc: function(e, t, n, i, o) {
        return (t /= o / 2) < 1 ? -i / 2 * (Math.sqrt(1 - t * t) - 1) + n : i / 2 * (Math.sqrt(1 - (t -= 2) * t) + 1) + n
    },
    easeInElastic: function(e, t, n, i, o) {
        var s = 1.70158,
            r = 0,
            a = i;
        if (0 == t) return n;
        if (1 == (t /= o)) return n + i;
        if (r || (r = .3 * o), a < Math.abs(i)) {
            a = i;
            var s = r / 4
        } else var s = r / (2 * Math.PI) * Math.asin(i / a);
        return -(a * Math.pow(2, 10 * (t -= 1)) * Math.sin((t * o - s) * (2 * Math.PI) / r)) + n
    },
    easeOutElastic: function(e, t, n, i, o) {
        var s = 1.70158,
            r = 0,
            a = i;
        if (0 == t) return n;
        if (1 == (t /= o)) return n + i;
        if (r || (r = .3 * o), a < Math.abs(i)) {
            a = i;
            var s = r / 4
        } else var s = r / (2 * Math.PI) * Math.asin(i / a);
        return a * Math.pow(2, -10 * t) * Math.sin((t * o - s) * (2 * Math.PI) / r) + i + n
    },
    easeInOutElastic: function(e, t, n, i, o) {
        var s = 1.70158,
            r = 0,
            a = i;
        if (0 == t) return n;
        if (2 == (t /= o / 2)) return n + i;
        if (r || (r = o * (.3 * 1.5)), a < Math.abs(i)) {
            a = i;
            var s = r / 4
        } else var s = r / (2 * Math.PI) * Math.asin(i / a);
        return t < 1 ? -.5 * (a * Math.pow(2, 10 * (t -= 1)) * Math.sin((t * o - s) * (2 * Math.PI) / r)) + n : a * Math.pow(2, -10 * (t -= 1)) * Math.sin((t * o - s) * (2 * Math.PI) / r) * .5 + i + n
    },
    easeInBack: function(e, t, n, i, o, s) {
        return void 0 == s && (s = 1.70158), i * (t /= o) * t * ((s + 1) * t - s) + n
    },
    easeOutBack: function(e, t, n, i, o, s) {
        return void 0 == s && (s = 1.70158), i * ((t = t / o - 1) * t * ((s + 1) * t + s) + 1) + n
    },
    easeInOutBack: function(e, t, n, i, o, s) {
        return void 0 == s && (s = 1.70158), (t /= o / 2) < 1 ? i / 2 * (t * t * (((s *= 1.525) + 1) * t - s)) + n : i / 2 * ((t -= 2) * t * (((s *= 1.525) + 1) * t + s) + 2) + n
    },
    easeInBounce: function(e, t, n, i, o) {
        return i - jQuery.easing.easeOutBounce(e, o - t, 0, i, o) + n
    },
    easeOutBounce: function(e, t, n, i, o) {
        return (t /= o) < 1 / 2.75 ? i * (7.5625 * t * t) + n : t < 2 / 2.75 ? i * (7.5625 * (t -= 1.5 / 2.75) * t + .75) + n : t < 2.5 / 2.75 ? i * (7.5625 * (t -= 2.25 / 2.75) * t + .9375) + n : i * (7.5625 * (t -= 2.625 / 2.75) * t + .984375) + n
    },
    easeInOutBounce: function(e, t, n, i, o) {
        return t < o / 2 ? .5 * jQuery.easing.easeInBounce(e, 2 * t, 0, i, o) + n : .5 * jQuery.easing.easeOutBounce(e, 2 * t - o, 0, i, o) + .5 * i + n
    }
}), function(e, t) {
    "use strict";
    "function" == typeof define && define.amd ? define(["jquery"], function(n) {
        return t(n, e, e.document, e.Math)
    }) : "object" == typeof exports && exports ? module.exports = t(require("jquery"), e, e.document, e.Math) : t(jQuery, e, e.document, e.Math)
}("undefined" != typeof window ? window : this, function(e, t, n, i, o) {
    "use strict";
    var s = "fullpage-wrapper",
        r = "." + s,
        a = "fp-responsive",
        l = "fp-notransition",
        c = "fp-destroyed",
        u = "fp-enabled",
        d = "fp-viewing",
        h = "active",
        f = "." + h,
        p = "fp-completely",
        m = "." + p,
        g = ".section",
        v = "fp-section",
        y = "." + v,
        b = y + f,
        w = y + ":first",
        T = y + ":last",
        x = "fp-tableCell",
        C = "." + x,
        D = "fp-auto-height",
        k = "fp-normal-scroll",
        S = "fp-nav",
        E = "#" + S,
        M = "fp-tooltip",
        N = "." + M,
        A = "fp-show-active",
        I = ".slide",
        O = "fp-slide",
        $ = "." + O,
        U = $ + f,
        j = "fp-slides",
        L = "." + j,
        H = "fp-slidesContainer",
        F = "." + H,
        R = "fp-table",
        P = "fp-slidesNav",
        _ = "." + P,
        q = _ + " a",
        B = "fp-controlArrow",
        V = "." + B,
        W = "fp-prev",
        z = "." + W,
        Y = B + " " + W,
        Q = V + z,
        X = "fp-next",
        J = "." + X,
        G = B + " " + X,
        K = V + J,
        Z = e(t),
        ee = e(n);
    e.fn.fullpage = function(B) {
        function z(t, n) {
            t || Jt(0), tn("autoScrolling", t, n);
            var i = e(b);
            B.autoScrolling && !B.scrollBar ? (sn.css({
                overflow: "hidden",
                height: "100%"
            }), X(In.recordHistory, "internal"), mn.css({
                "-ms-touch-action": "none",
                "touch-action": "none"
            }), i.length && Jt(i.position().top)) : (sn.css({
                overflow: "visible",
                height: "initial"
            }), X(!1, "internal"), mn.css({
                "-ms-touch-action": "",
                "touch-action": ""
            }), i.length && sn.scrollTop(i.position().top))
        }

        function X(e, t) {
            tn("recordHistory", e, t)
        }

        function J(e, t) {
            tn("scrollingSpeed", e, t)
        }

        function te(e, t) {
            tn("fitToSection", e, t)
        }

        function ne(e) {
            B.lockAnchors = e
        }

        function ie(e) {
            e ? (qt(), Bt()) : (_t(), Vt())
        }

        function oe(t, n) {
            "undefined" != typeof n ? (n = n.replace(/ /g, "").split(","), e.each(n, function(e, n) {
                Kt(t, n, "m")
            })) : (Kt(t, "all", "m"), t ? (ie(!0), Wt()) : (ie(!1), zt()))
        }

        function se(t, n) {
            "undefined" != typeof n ? (n = n.replace(/ /g, "").split(","), e.each(n, function(e, n) {
                Kt(t, n, "k")
            })) : (Kt(t, "all", "k"), B.keyboardScrolling = t)
        }

        function re() {
            var t = e(b).prev(y);
            t.length || !B.loopTop && !B.continuousVertical || (t = e(y).last()), t.length && Be(t, null, !0)
        }

        function ae() {
            var t = e(b).next(y);
            t.length || !B.loopBottom && !B.continuousVertical || (t = e(y).first()), t.length && Be(t, null, !1)
        }

        function le(e, t) {
            J(0, "internal"), ce(e, t), J(In.scrollingSpeed, "internal")
        }

        function ce(e, t) {
            var n = It(e);
            "undefined" != typeof t ? $t(e, t) : n.length > 0 && Be(n)
        }

        function ue(e) {
            Pe("right", e)
        }

        function de(e) {
            Pe("left", e)
        }

        function he(t) {
            if (!mn.hasClass(c)) {
                vn = !0, gn = Z.height(), e(y).each(function() {
                    var t = e(this).find(L),
                        n = e(this).find($);
                    B.verticalCentered && e(this).find(C).css("height", Nt(e(this)) + "px"), e(this).css("height", gn + "px"), n.length > 1 && mt(t, t.find(U))
                }), B.scrollOverflow && xn.createScrollBarForAll();
                var n = e(b),
                    i = n.index(y);
                i && le(i + 1), vn = !1, e.isFunction(B.afterResize) && t && B.afterResize.call(mn), e.isFunction(B.afterReBuild) && !t && B.afterReBuild.call(mn)
            }
        }

        function fe(t) {
            var n = rn.hasClass(a);
            t ? n || (z(!1, "internal"), te(!1, "internal"), e(E).hide(), rn.addClass(a), e.isFunction(B.afterResponsive) && B.afterResponsive.call(mn, t)) : n && (z(In.autoScrolling, "internal"), te(In.autoScrolling, "internal"), e(E).show(), rn.removeClass(a), e.isFunction(B.afterResponsive) && B.afterResponsive.call(mn, t))
        }

        function pe() {
            B.css3 && (B.css3 = Pt()), B.scrollBar = B.scrollBar || B.hybrid, ge(), ve(), oe(!0), z(B.autoScrolling, "internal"), wt(), Rt(), "complete" === n.readyState && tt(), Z.on("load", tt)
        }

        function me() {
            Z.on("scroll", Ne).on("hashchange", nt).blur(ut).resize(bt), ee.keydown(ot).keyup(rt).on("click touchstart", E + " a", dt).on("click touchstart", q, ht).on("click", N, st), e(y).on("click touchstart", V, ct), B.normalScrollElements && (ee.on("mouseenter touchstart", B.normalScrollElements, function() {
                oe(!1)
            }), ee.on("mouseleave touchend", B.normalScrollElements, function() {
                oe(!0)
            }))
        }

        function ge() {
            var t = mn.find(B.sectionSelector);
            B.anchors.length || (B.anchors = t.filter("[data-anchor]").map(function() {
                return e(this).data("anchor").toString()
            }).get()), B.navigationTooltips.length || (B.navigationTooltips = t.filter("[data-tooltip]").map(function() {
                return e(this).data("tooltip").toString()
            }).get())
        }

        function ve() {
            mn.css({
                height: "100%",
                position: "relative"
            }), mn.addClass(s), e("html").addClass(u), gn = Z.height(), mn.removeClass(c), Te(), e(y).each(function(t) {
                var n = e(this),
                    i = n.find($),
                    o = i.length;
                be(n, t), we(n, t), o > 0 ? ye(n, i, o) : B.verticalCentered && Mt(n)
            }), B.fixedElements && B.css3 && e(B.fixedElements).appendTo(rn), B.navigation && Ce(), De(), B.scrollOverflow ? xn = B.scrollOverflowHandler.init(B) : Ee()
        }

        function ye(t, n, i) {
            var o = 100 * i,
                s = 100 / i;
            n.wrapAll('<div class="' + H + '" />'), n.parent().wrap('<div class="' + j + '" />'), t.find(F).css("width", o + "%"), i > 1 && (B.controlArrows && xe(t), B.slidesNavigation && jt(t, i)), n.each(function(t) {
                e(this).css("width", s + "%"), B.verticalCentered && Mt(e(this))
            });
            var r = t.find(U);
            r.length && (0 !== e(b).index(y) || 0 === e(b).index(y) && 0 !== r.index()) ? Xt(r, "internal") : n.eq(0).addClass(h)
        }

        function be(t, n) {
            n || 0 !== e(b).length || t.addClass(h), dn = e(b), t.css("height", gn + "px"), B.paddingTop && t.css("padding-top", B.paddingTop), B.paddingBottom && t.css("padding-bottom", B.paddingBottom), "undefined" != typeof B.sectionsColor[n] && t.css("background-color", B.sectionsColor[n]), "undefined" != typeof B.anchors[n] && t.attr("data-anchor", B.anchors[n])
        }

        function we(t, n) {
            "undefined" != typeof B.anchors[n] && t.hasClass(h) && kt(B.anchors[n], n), B.menu && B.css3 && e(B.menu).closest(r).length && e(B.menu).appendTo(rn)
        }

        function Te() {
            mn.find(B.sectionSelector).addClass(v), mn.find(B.slideSelector).addClass(O)
        }

        function xe(e) {
            e.find(L).after('<div class="' + Y + '"></div><div class="' + G + '"></div>'), "#fff" != B.controlArrowColor && (e.find(K).css("border-color", "transparent transparent transparent " + B.controlArrowColor), e.find(Q).css("border-color", "transparent " + B.controlArrowColor + " transparent transparent")), B.loopHorizontal || e.find(Q).hide()
        }

        function Ce() {
            rn.append('<div id="' + S + '"><ul></ul></div>');
            var t = e(E);
            t.addClass(function() {
                return B.showActiveTooltip ? A + " " + B.navigationPosition : B.navigationPosition
            });
            for (var n = 0; n < e(y).length; n++) {
                var i = "";
                B.anchors.length && (i = B.anchors[n]);
                var o = '<li><a href="#' + i + '"><span></span></a>',
                    s = B.navigationTooltips[n];
                "undefined" != typeof s && "" !== s && (o += '<div class="' + M + " " + B.navigationPosition + '">' + s + "</div>"), o += "</li>", t.find("ul").append(o)
            }
            e(E).css("margin-top", "-" + e(E).height() / 2 + "px"), e(E).find("li").eq(e(b).index(y)).find("a").addClass(h)
        }

        function De() {
            mn.find('iframe[src*="youtube.com/embed/"]').each(function() {
                ke(e(this), "enablejsapi=1")
            })
        }

        function ke(e, t) {
            var n = e.attr("src");
            e.attr("src", n + Se(n) + t)
        }

        function Se(e) {
            return /\?/.test(e) ? "&" : "?"
        }

        function Ee() {
            var t = e(b);
            t.addClass(p), Je(t), Ge(t), B.scrollOverflow && B.scrollOverflowHandler.afterLoad(), Me() && e.isFunction(B.afterLoad) && B.afterLoad.call(t, t.data("anchor"), t.index(y) + 1), e.isFunction(B.afterRender) && B.afterRender.call(mn)
        }

        function Me() {
            var e = It(it().section);
            return !e.length || e.length && e.index() === dn.index()
        }

        function Ne() {
            var t;
            if (!B.autoScrolling || B.scrollBar) {
                var i = Z.scrollTop(),
                    o = Oe(i),
                    s = 0,
                    r = i + Z.height() / 2,
                    a = rn.height() - Z.height() === i,
                    l = n.querySelectorAll(y);
                if (a) s = l.length - 1;
                else if (i)
                    for (var c = 0; c < l.length; ++c) {
                        var u = l[c];
                        u.offsetTop <= r && (s = c)
                    } else s = 0;
                if (Ie(o) && (e(b).hasClass(p) || e(b).addClass(p).siblings().removeClass(p)), t = e(l).eq(s), !t.hasClass(h)) {
                    On = !0;
                    var d, f, m = e(b),
                        g = m.index(y) + 1,
                        v = St(t),
                        w = t.data("anchor"),
                        T = t.index(y) + 1,
                        x = t.find(U);
                    x.length && (f = x.data("anchor"), d = x.index()), bn && (t.addClass(h).siblings().removeClass(h), e.isFunction(B.onLeave) && B.onLeave.call(m, g, T, v), e.isFunction(B.afterLoad) && B.afterLoad.call(t, w, T), Ze(m), Je(t), Ge(t), kt(w, T - 1), B.anchors.length && (ln = w), Lt(d, f, w, T)), clearTimeout(Sn), Sn = setTimeout(function() {
                        On = !1
                    }, 100)
                }
                B.fitToSection && (clearTimeout(En), En = setTimeout(function() {
                    B.fitToSection && e(b).outerHeight() <= gn && Ae()
                }, B.fitToSectionDelay))
            }
        }

        function Ae() {
            bn && (vn = !0, Be(e(b)), vn = !1)
        }

        function Ie(t) {
            var n = e(b).position().top,
                i = n + Z.height();
            return "up" == t ? i >= Z.scrollTop() + Z.height() : n <= Z.scrollTop()
        }

        function Oe(e) {
            var t = e > $n ? "down" : "up";
            return $n = e, Rn = e, t
        }

        function $e(t) {
            if (Tn.m[t]) {
                var n = "down" === t ? ae : re;
                if (B.scrollOverflow) {
                    var i = B.scrollOverflowHandler.scrollable(e(b)),
                        o = "down" === t ? "bottom" : "top";
                    if (i.length > 0) {
                        if (!B.scrollOverflowHandler.isScrolled(o, i)) return !0;
                        n()
                    } else n()
                } else n()
            }
        }

        function Ue(e) {
            var t = e.originalEvent;
            B.autoScrolling && Le(t) && e.preventDefault()
        }

        function je(t) {
            var n = t.originalEvent,
                o = e(n.target).closest(y);
            if (Le(n)) {
                B.autoScrolling && t.preventDefault();
                var s = Qt(n);
                Ln = s.y, Hn = s.x, o.find(L).length && i.abs(jn - Hn) > i.abs(Un - Ln) ? !hn && i.abs(jn - Hn) > Z.outerWidth() / 100 * B.touchSensitivity && (jn > Hn ? Tn.m.right && ue(o) : Tn.m.left && de(o)) : B.autoScrolling && bn && i.abs(Un - Ln) > Z.height() / 100 * B.touchSensitivity && (Un > Ln ? $e("down") : Ln > Un && $e("up"))
            }
        }

        function Le(e) {
            return "undefined" == typeof e.pointerType || "mouse" != e.pointerType
        }

        function He(e) {
            var t = e.originalEvent;
            if (B.fitToSection && sn.stop(), Le(t)) {
                var n = Qt(t);
                Un = n.y, jn = n.x
            }
        }

        function Fe(e, t) {
            for (var n = 0, o = e.slice(i.max(e.length - t, 1)), s = 0; s < o.length; s++) n += o[s];
            return i.ceil(n / t)
        }

        function Re(n) {
            var o = (new Date).getTime(),
                s = e(m).hasClass(k);
            if (B.autoScrolling && !un && !s) {
                n = n || t.event;
                var r = n.wheelDelta || -n.deltaY || -n.detail,
                    a = i.max(-1, i.min(1, r)),
                    l = "undefined" != typeof n.wheelDeltaX || "undefined" != typeof n.deltaX,
                    c = i.abs(n.wheelDeltaX) < i.abs(n.wheelDelta) || i.abs(n.deltaX) < i.abs(n.deltaY) || !l;
                wn.length > 149 && wn.shift(), wn.push(i.abs(r)), B.scrollBar && (n.preventDefault ? n.preventDefault() : n.returnValue = !1);
                var u = o - Fn;
                if (Fn = o, u > 200 && (wn = []), bn) {
                    var d = Fe(wn, 10),
                        h = Fe(wn, 70),
                        f = d >= h;
                    f && c && $e(a < 0 ? "down" : "up")
                }
                return !1
            }
            B.fitToSection && sn.stop()
        }

        function Pe(t, n) {
            var i = "undefined" == typeof n ? e(b) : n,
                o = i.find(L),
                s = o.find($).length;
            if (!(!o.length || hn || s < 2)) {
                var r = o.find(U),
                    a = null;
                if (a = "left" === t ? r.prev($) : r.next($), !a.length) {
                    if (!B.loopHorizontal) return;
                    a = "left" === t ? r.siblings(":last") : r.siblings(":first")
                }
                hn = !0, mt(o, a, t)
            }
        }

        function _e() {
            e(U).each(function() {
                Xt(e(this), "internal")
            })
        }

        function qe(e) {
            var t = e.position(),
                n = t.top,
                i = t.top > Rn,
                o = n - gn + e.outerHeight(),
                s = B.bigSectionsDestination;
            return e.outerHeight() > gn ? (i || s) && "bottom" !== s || (n = o) : (i || vn && e.is(":last-child")) && (n = o), Rn = n, n
        }

        function Be(t, n, i) {
            if ("undefined" != typeof t) {
                var o, s, r = qe(t),
                    a = {
                        element: t,
                        callback: n,
                        isMovementUp: i,
                        dtop: r,
                        yMovement: St(t),
                        anchorLink: t.data("anchor"),
                        sectionIndex: t.index(y),
                        activeSlide: t.find(U),
                        activeSection: e(b),
                        leavingSection: e(b).index(y) + 1,
                        localIsResizing: vn
                    };
                a.activeSection.is(t) && !vn || B.scrollBar && Z.scrollTop() === a.dtop && !t.hasClass(D) || (a.activeSlide.length && (o = a.activeSlide.data("anchor"), s = a.activeSlide.index()), e.isFunction(B.onLeave) && !a.localIsResizing && B.onLeave.call(a.activeSection, a.leavingSection, a.sectionIndex + 1, a.yMovement) === !1 || (B.autoScrolling && B.continuousVertical && "undefined" != typeof a.isMovementUp && (!a.isMovementUp && "up" == a.yMovement || a.isMovementUp && "down" == a.yMovement) && (a = ze(a)), a.localIsResizing || Ze(a.activeSection), B.scrollOverflow && B.scrollOverflowHandler.beforeLeave(), t.addClass(h).siblings().removeClass(h), Je(t), B.scrollOverflow && B.scrollOverflowHandler.onLeave(), bn = !1, Lt(s, o, a.anchorLink, a.sectionIndex), Ve(a), ln = a.anchorLink, kt(a.anchorLink, a.sectionIndex)))
            }
        }

        function Ve(t) {
            if (B.css3 && B.autoScrolling && !B.scrollBar) {
                var n = "translate3d(0px, -" + i.round(t.dtop) + "px, 0px)";
                At(n, !0), B.scrollingSpeed ? (clearTimeout(Dn), Dn = setTimeout(function() {
                    Qe(t)
                }, B.scrollingSpeed)) : Qe(t)
            } else {
                var o = We(t);
                e(o.element).animate(o.options, B.scrollingSpeed, B.easing).promise().done(function() {
                    B.scrollBar ? setTimeout(function() {
                        Qe(t)
                    }, 30) : Qe(t)
                })
            }
        }

        function We(e) {
            var t = {};
            return B.autoScrolling && !B.scrollBar ? (t.options = {
                top: -e.dtop
            }, t.element = r) : (t.options = {
                scrollTop: e.dtop
            }, t.element = "html, body"), t
        }

        function ze(t) {
            return t.isMovementUp ? e(b).before(t.activeSection.nextAll(y)) : e(b).after(t.activeSection.prevAll(y).get().reverse()), Jt(e(b).position().top), _e(), t.wrapAroundElements = t.activeSection, t.dtop = t.element.position().top, t.yMovement = St(t.element), t.leavingSection = t.activeSection.index(y) + 1, t.sectionIndex = t.element.index(y), t
        }

        function Ye(t) {
            t.wrapAroundElements && t.wrapAroundElements.length && (t.isMovementUp ? e(w).before(t.wrapAroundElements) : e(T).after(t.wrapAroundElements), Jt(e(b).position().top), _e())
        }

        function Qe(t) {
            Ye(t), e.isFunction(B.afterLoad) && !t.localIsResizing && B.afterLoad.call(t.element, t.anchorLink, t.sectionIndex + 1), B.scrollOverflow && B.scrollOverflowHandler.afterLoad(), t.localIsResizing || Ge(t.element), t.element.addClass(p).siblings().removeClass(p), bn = !0, e.isFunction(t.callback) && t.callback.call(this)
        }

        function Xe(e, t) {
            e.attr(t, e.data(t)).removeAttr("data-" + t)
        }

        function Je(t) {
            if (B.lazyLoading) {
                var n, i = et(t);
                i.find("img[data-src], img[data-srcset], source[data-src], video[data-src], audio[data-src], iframe[data-src]").each(function() {
                    if (n = e(this), e.each(["src", "srcset"], function(e, t) {
                        var i = n.attr("data-" + t);
                        "undefined" != typeof i && i && Xe(n, t)
                    }), n.is("source")) {
                        var t = n.closest("video").length ? "video" : "audio";
                        n.closest(t).get(0).load()
                    }
                })
            }
        }

        function Ge(t) {
            var n = et(t);
            n.find("video, audio").each(function() {
                var t = e(this).get(0);
                t.hasAttribute("data-autoplay") && "function" == typeof t.play && t.play()
            }), n.find('iframe[src*="youtube.com/embed/"]').each(function() {
                var t = e(this).get(0);
                t.hasAttribute("data-autoplay") && Ke(t), t.onload = function() {
                    t.hasAttribute("data-autoplay") && Ke(t)
                }
            })
        }

        function Ke(e) {
            e.contentWindow.postMessage('{"event":"command","func":"playVideo","args":""}', "*")
        }

        function Ze(t) {
            var n = et(t);
            n.find("video, audio").each(function() {
                var t = e(this).get(0);
                t.hasAttribute("data-keepplaying") || "function" != typeof t.pause || t.pause()
            }), n.find('iframe[src*="youtube.com/embed/"]').each(function() {
                var t = e(this).get(0);
                /youtube\.com\/embed\//.test(e(this).attr("src")) && !t.hasAttribute("data-keepplaying") && e(this).get(0).contentWindow.postMessage('{"event":"command","func":"pauseVideo","args":""}', "*")
            })
        }

        function et(t) {
            var n = t.find(U);
            return n.length && (t = e(n)), t
        }

        function tt() {
            var e = it(),
                t = e.section,
                n = e.slide;
            t && (B.animateAnchor ? $t(t, n) : le(t, n))
        }

        function nt() {
            if (!On && !B.lockAnchors) {
                var e = it(),
                    t = e.section,
                    n = e.slide,
                    i = "undefined" == typeof ln,
                    o = "undefined" == typeof ln && "undefined" == typeof n && !hn;
                t.length && (t && t !== ln && !i || o || !hn && cn != n) && $t(t, n)
            }
        }

        function it() {
            var e = t.location.hash,
                n = e.replace("#", "").split("/"),
                i = e.indexOf("#/") > -1;
            return {
                section: i ? "/" + n[1] : decodeURIComponent(n[0]),
                slide: i ? decodeURIComponent(n[2]) : decodeURIComponent(n[1])
            }
        }

        function ot(t) {
            clearTimeout(Mn);
            var n = e(":focus");
            if (!n.is("textarea") && !n.is("input") && !n.is("select") && "true" !== n.attr("contentEditable") && "" !== n.attr("contentEditable") && B.keyboardScrolling && B.autoScrolling) {
                var i = t.which,
                    o = [40, 38, 32, 33, 34];
                e.inArray(i, o) > -1 && t.preventDefault(), un = t.ctrlKey, Mn = setTimeout(function() {
                    ft(t)
                }, 150)
            }
        }

        function st() {
            e(this).prev().trigger("click")
        }

        function rt(e) {
            yn && (un = e.ctrlKey)
        }

        function at(e) {
            2 == e.which && (Pn = e.pageY, mn.on("mousemove", pt))
        }

        function lt(e) {
            2 == e.which && mn.off("mousemove")
        }

        function ct() {
            var t = e(this).closest(y);
            e(this).hasClass(W) ? Tn.m.left && de(t) : Tn.m.right && ue(t)
        }

        function ut() {
            yn = !1, un = !1
        }

        function dt(t) {
            t.preventDefault();
            var n = e(this).parent().index();
            Be(e(y).eq(n))
        }

        function ht(t) {
            t.preventDefault();
            var n = e(this).closest(y).find(L),
                i = n.find($).eq(e(this).closest("li").index());
            mt(n, i)
        }

        function ft(t) {
            var n = t.shiftKey;
            if (bn || !([37, 39].indexOf(t.which) < 0)) switch (t.which) {
                case 38:
                case 33:
                    Tn.k.up && re();
                    break;
                case 32:
                    if (n && Tn.k.up) {
                        re();
                        break
                    }
                case 40:
                case 34:
                    Tn.k.down && ae();
                    break;
                case 36:
                    Tn.k.up && ce(1);
                    break;
                case 35:
                    Tn.k.down && ce(e(y).length);
                    break;
                case 37:
                    Tn.k.left && de();
                    break;
                case 39:
                    Tn.k.right && ue();
                    break;
                default:
                    return
            }
        }

        function pt(e) {
            bn && (e.pageY < Pn && Tn.m.up ? re() : e.pageY > Pn && Tn.m.down && ae()), Pn = e.pageY
        }

        function mt(t, n, i) {
            var o = t.closest(y),
                s = {
                    slides: t,
                    destiny: n,
                    direction: i,
                    destinyPos: n.position(),
                    slideIndex: n.index(),
                    section: o,
                    sectionIndex: o.index(y),
                    anchorLink: o.data("anchor"),
                    slidesNav: o.find(_),
                    slideAnchor: Ft(n),
                    prevSlide: o.find(U),
                    prevSlideIndex: o.find(U).index(),
                    localIsResizing: vn
                };
            return s.xMovement = Et(s.prevSlideIndex, s.slideIndex), s.localIsResizing || (bn = !1), B.onSlideLeave && !s.localIsResizing && "none" !== s.xMovement && e.isFunction(B.onSlideLeave) && B.onSlideLeave.call(s.prevSlide, s.anchorLink, s.sectionIndex + 1, s.prevSlideIndex, s.direction, s.slideIndex) === !1 ? void(hn = !1) : (n.addClass(h).siblings().removeClass(h), s.localIsResizing || (Ze(s.prevSlide), Je(n)), !B.loopHorizontal && B.controlArrows && (o.find(Q).toggle(0 !== s.slideIndex), o.find(K).toggle(!n.is(":last-child"))), o.hasClass(h) && !s.localIsResizing && Lt(s.slideIndex, s.slideAnchor, s.anchorLink, s.sectionIndex), void vt(t, s, !0))
        }

        function gt(t) {
            yt(t.slidesNav, t.slideIndex), t.localIsResizing || (e.isFunction(B.afterSlideLoad) && B.afterSlideLoad.call(t.destiny, t.anchorLink, t.sectionIndex + 1, t.slideAnchor, t.slideIndex), bn = !0, Ge(t.destiny)), hn = !1
        }

        function vt(e, t, n) {
            var o = t.destinyPos;
            if (B.css3) {
                var s = "translate3d(-" + i.round(o.left) + "px, 0px, 0px)";
                Tt(e.find(F)).css(Gt(s)), kn = setTimeout(function() {
                    n && gt(t)
                }, B.scrollingSpeed, B.easing)
            } else e.animate({
                scrollLeft: i.round(o.left)
            }, B.scrollingSpeed, B.easing, function() {
                n && gt(t)
            })
        }

        function yt(e, t) {
            e.find(f).removeClass(h), e.find("li").eq(t).find("a").addClass(h)
        }

        function bt() {
            if (wt(), fn) {
                var t = e(n.activeElement);
                if (!t.is("textarea") && !t.is("input") && !t.is("select")) {
                    var o = Z.height();
                    i.abs(o - _n) > 20 * i.max(_n, o) / 100 && (he(!0), _n = o)
                }
            } else clearTimeout(Cn), Cn = setTimeout(function() {
                he(!0)
            }, 350)
        }

        function wt() {
            var e = B.responsive || B.responsiveWidth,
                t = B.responsiveHeight,
                n = e && Z.outerWidth() < e,
                i = t && Z.height() < t;
            e && t ? fe(n || i) : e ? fe(n) : t && fe(i)
        }

        function Tt(e) {
            var t = "all " + B.scrollingSpeed + "ms " + B.easingcss3;
            return e.removeClass(l), e.css({
                "-webkit-transition": t,
                transition: t
            })
        }

        function xt(e) {
            return e.addClass(l)
        }

        function Ct(t, n) {
            B.navigation && (e(E).find(f).removeClass(h), t ? e(E).find('a[href="#' + t + '"]').addClass(h) : e(E).find("li").eq(n).find("a").addClass(h))
        }

        function Dt(t) {
            B.menu && (e(B.menu).find(f).removeClass(h), e(B.menu).find('[data-menuanchor="' + t + '"]').addClass(h))
        }

        function kt(e, t) {
            Dt(e), Ct(e, t)
        }

        function St(t) {
            var n = e(b).index(y),
                i = t.index(y);
            return n == i ? "none" : n > i ? "up" : "down"
        }

        function Et(e, t) {
            return e == t ? "none" : e > t ? "left" : "right"
        }

        function Mt(e) {
            e.hasClass(R) || e.addClass(R).wrapInner('<div class="' + x + '" style="height:' + Nt(e) + 'px;" />')
        }

        function Nt(e) {
            var t = gn;
            if (B.paddingTop || B.paddingBottom) {
                var n = e;
                n.hasClass(v) || (n = e.closest(y));
                var i = parseInt(n.css("padding-top")) + parseInt(n.css("padding-bottom"));
                t = gn - i
            }
            return t
        }

        function At(e, t) {
            t ? Tt(mn) : xt(mn), mn.css(Gt(e)), setTimeout(function() {
                mn.removeClass(l)
            }, 10)
        }

        function It(t) {
            if (!t) return [];
            var n = mn.find(y + '[data-anchor="' + t + '"]');
            return n.length || (n = e(y).eq(t - 1)), n
        }

        function Ot(e, t) {
            var n = t.find(L),
                i = n.find($ + '[data-anchor="' + e + '"]');
            return i.length || (i = n.find($).eq(e)), i
        }

        function $t(e, t) {
            var n = It(e);
            n.length && ("undefined" == typeof t && (t = 0), e === ln || n.hasClass(h) ? Ut(n, t) : Be(n, function() {
                Ut(n, t)
            }))
        }

        function Ut(e, t) {
            if ("undefined" != typeof t) {
                var n = e.find(L),
                    i = Ot(t, e);
                i.length && mt(n, i)
            }
        }

        function jt(e, t) {
            e.append('<div class="' + P + '"><ul></ul></div>');
            var n = e.find(_);
            n.addClass(B.slidesNavPosition);
            for (var i = 0; i < t; i++) n.find("ul").append('<li><a href="#"><span></span></a></li>');
            n.css("margin-left", "-" + n.width() / 2 + "px"), n.find("li").first().find("a").addClass(h)
        }

        function Lt(e, t, n, i) {
            var o = "";
            B.anchors.length && !B.lockAnchors && (e ? ("undefined" != typeof n && (o = n), "undefined" == typeof t && (t = e), cn = t, Ht(o + "/" + t)) : "undefined" != typeof e ? (cn = t, Ht(n)) : Ht(n)), Rt()
        }

        function Ht(e) {
            if (B.recordHistory) location.hash = e;
            else if (fn || pn) t.history.replaceState(o, o, "#" + e);
            else {
                var n = t.location.href.split("#")[0];
                t.location.replace(n + "#" + e)
            }
        }

        function Ft(e) {
            var t = e.data("anchor"),
                n = e.index();
            return "undefined" == typeof t && (t = n), t
        }

        function Rt() {
            var t = e(b),
                n = t.find(U),
                i = Ft(t),
                o = Ft(n),
                s = String(i);
            n.length && (s = s + "-" + o), s = s.replace("/", "-").replace("#", "");
            var r = new RegExp("\\b\\s?" + d + "-[^\\s]+\\b", "g");
            rn[0].className = rn[0].className.replace(r, ""), rn.addClass(d + "-" + s)
        }

        function Pt() {
            var e, i = n.createElement("p"),
                s = {
                    webkitTransform: "-webkit-transform",
                    OTransform: "-o-transform",
                    msTransform: "-ms-transform",
                    MozTransform: "-moz-transform",
                    transform: "transform"
                };
            n.body.insertBefore(i, null);
            for (var r in s) i.style[r] !== o && (i.style[r] = "translate3d(1px,1px,1px)", e = t.getComputedStyle(i).getPropertyValue(s[r]));
            return n.body.removeChild(i), e !== o && e.length > 0 && "none" !== e
        }

        function _t() {
            n.addEventListener ? (n.removeEventListener("mousewheel", Re, !1), n.removeEventListener("wheel", Re, !1), n.removeEventListener("MozMousePixelScroll", Re, !1)) : n.detachEvent("onmousewheel", Re)
        }

        function qt() {
            var e, i = "";
            t.addEventListener ? e = "addEventListener" : (e = "attachEvent", i = "on");
            var s = "onwheel" in n.createElement("div") ? "wheel" : n.onmousewheel !== o ? "mousewheel" : "DOMMouseScroll";
            "DOMMouseScroll" == s ? n[e](i + "MozMousePixelScroll", Re, !1) : n[e](i + s, Re, !1)
        }

        function Bt() {
            mn.on("mousedown", at).on("mouseup", lt)
        }

        function Vt() {
            mn.off("mousedown", at).off("mouseup", lt)
        }

        function Wt() {
            (fn || pn) && (B.autoScrolling && rn.off(An.touchmove).on(An.touchmove, Ue), e(r).off(An.touchstart).on(An.touchstart, He).off(An.touchmove).on(An.touchmove, je))
        }

        function zt() {
            (fn || pn) && (B.autoScrolling && rn.off(An.touchmove), e(r).off(An.touchstart).off(An.touchmove))
        }

        function Yt() {
            var e;
            return e = t.PointerEvent ? {
                down: "pointerdown",
                move: "pointermove"
            } : {
                down: "MSPointerDown",
                move: "MSPointerMove"
            }
        }

        function Qt(e) {
            var t = [];
            return t.y = "undefined" != typeof e.pageY && (e.pageY || e.pageX) ? e.pageY : e.touches[0].pageY, t.x = "undefined" != typeof e.pageX && (e.pageY || e.pageX) ? e.pageX : e.touches[0].pageX, pn && Le(e) && B.scrollBar && (t.y = e.touches[0].pageY, t.x = e.touches[0].pageX), t
        }

        function Xt(e, t) {
            J(0, "internal"), "undefined" != typeof t && (vn = !0), mt(e.closest(L), e), "undefined" != typeof t && (vn = !1), J(In.scrollingSpeed, "internal")
        }

        function Jt(e) {
            var t = i.round(e);
            if (B.css3 && B.autoScrolling && !B.scrollBar) {
                var n = "translate3d(0px, -" + t + "px, 0px)";
                At(n, !1)
            } else B.autoScrolling && !B.scrollBar ? mn.css("top", -t) : sn.scrollTop(t)
        }

        function Gt(e) {
            return {
                "-webkit-transform": e,
                "-moz-transform": e,
                "-ms-transform": e,
                transform: e
            }
        }

        function Kt(t, n, i) {
            "all" !== n ? Tn[i][n] = t : e.each(Object.keys(Tn[i]), function(e, n) {
                Tn[i][n] = t
            })
        }

        function Zt(t) {
            z(!1, "internal"), oe(!1), se(!1), mn.addClass(c), clearTimeout(kn), clearTimeout(Dn), clearTimeout(Cn), clearTimeout(Sn), clearTimeout(En), Z.off("scroll", Ne).off("hashchange", nt).off("resize", bt), ee.off("click touchstart", E + " a").off("mouseenter", E + " li").off("mouseleave", E + " li").off("click touchstart", q).off("mouseover", B.normalScrollElements).off("mouseout", B.normalScrollElements), e(y).off("click touchstart", V), clearTimeout(kn), clearTimeout(Dn), t && en()
        }

        function en() {
            Jt(0), mn.find("img[data-src], source[data-src], audio[data-src], iframe[data-src]").each(function() {
                Xe(e(this), "src")
            }), mn.find("img[data-srcset]").each(function() {
                Xe(e(this), "srcset")
            }), e(E + ", " + _ + ", " + V).remove(), e(y).css({
                height: "",
                "background-color": "",
                padding: ""
            }), e($).css({
                width: ""
            }), mn.css({
                height: "",
                position: "",
                "-ms-touch-action": "",
                "touch-action": ""
            }), sn.css({
                overflow: "",
                height: ""
            }), e("html").removeClass(u), rn.removeClass(a), e.each(rn.get(0).className.split(/\s+/), function(e, t) {
                0 === t.indexOf(d) && rn.removeClass(t)
            }), e(y + ", " + $).each(function() {
                B.scrollOverflowHandler && B.scrollOverflowHandler.remove(e(this)), e(this).removeClass(R + " " + h)
            }), xt(mn), mn.find(C + ", " + F + ", " + L).each(function() {
                e(this).replaceWith(this.childNodes)
            }), mn.css({
                "-webkit-transition": "none",
                transition: "none"
            }), sn.scrollTop(0);
            var t = [v, O, H];
            e.each(t, function(t, n) {
                e("." + n).removeClass(n)
            })
        }

        function tn(e, t, n) {
            B[e] = t, "internal" !== n && (In[e] = t)
        }

        function nn() {
            var t = ["fadingEffect", "continuousHorizontal", "scrollHorizontally", "interlockedSlides", "resetSliders", "responsiveSlides", "offsetSections", "dragAndMove", "scrollOverflowReset", "parallax"];
            return e("html").hasClass(u) ? void on("error", "Fullpage.js can only be initialized once and you are doing it multiple times!") : (B.continuousVertical && (B.loopTop || B.loopBottom) && (B.continuousVertical = !1, on("warn", "Option `loopTop/loopBottom` is mutually exclusive with `continuousVertical`; `continuousVertical` disabled")), B.scrollBar && B.scrollOverflow && on("warn", "Option `scrollBar` is mutually exclusive with `scrollOverflow`. Sections with scrollOverflow might not work well in Firefox"), !B.continuousVertical || !B.scrollBar && B.autoScrolling || (B.continuousVertical = !1, on("warn", "Scroll bars (`scrollBar:true` or `autoScrolling:false`) are mutually exclusive with `continuousVertical`; `continuousVertical` disabled")), B.scrollOverflow && !B.scrollOverflowHandler && (B.scrollOverflow = !1, on("error", "The option `scrollOverflow:true` requires the file `scrolloverflow.min.js`. Please include it before fullPage.js.")), e.each(t, function(e, t) {
                B[t] && on("warn", "fullpage.js extensions require jquery.fullpage.extensions.min.js file instead of the usual jquery.fullpage.js. Requested: " + t)
            }), void e.each(B.anchors, function(t, n) {
                var i = ee.find("[name]").filter(function() {
                        return e(this).attr("name") && e(this).attr("name").toLowerCase() == n.toLowerCase()
                    }),
                    o = ee.find("[id]").filter(function() {
                        return e(this).attr("id") && e(this).attr("id").toLowerCase() == n.toLowerCase()
                    });
                (o.length || i.length) && (on("error", "data-anchor tags can not have the same value as any `id` element on the site (or `name` element for IE)."), o.length && on("error", '"' + n + '" is is being used by another element `id` property'), i.length && on("error", '"' + n + '" is is being used by another element `name` property'))
            }))
        }

        function on(e, t) {
            console && console[e] && console[e]("fullPage: " + t)
        }
        if (e("html").hasClass(u)) return void nn();
        var sn = e("html, body"),
            rn = e("body"),
            an = e.fn.fullpage;
        B = e.extend({
            menu: !1,
            anchors: [],
            lockAnchors: !1,
            navigation: !1,
            navigationPosition: "right",
            navigationTooltips: [],
            showActiveTooltip: !1,
            slidesNavigation: !1,
            slidesNavPosition: "bottom",
            scrollBar: !1,
            hybrid: !1,
            css3: !0,
            scrollingSpeed: 700,
            autoScrolling: !0,
            fitToSection: !0,
            fitToSectionDelay: 1e3,
            easing: "easeInOutCubic",
            easingcss3: "ease",
            loopBottom: !1,
            loopTop: !1,
            loopHorizontal: !0,
            continuousVertical: !1,
            continuousHorizontal: !1,
            scrollHorizontally: !1,
            interlockedSlides: !1,
            dragAndMove: !1,
            offsetSections: !1,
            resetSliders: !1,
            fadingEffect: !1,
            normalScrollElements: null,
            scrollOverflow: !1,
            scrollOverflowReset: !1,
            scrollOverflowHandler: e.fn.fp_scrolloverflow ? e.fn.fp_scrolloverflow.iscrollHandler : null,
            scrollOverflowOptions: null,
            touchSensitivity: 5,
            normalScrollElementTouchThreshold: 5,
            bigSectionsDestination: null,
            keyboardScrolling: !0,
            animateAnchor: !0,
            recordHistory: !0,
            controlArrows: !0,
            controlArrowColor: "#fff",
            verticalCentered: !0,
            sectionsColor: [],
            paddingTop: 0,
            paddingBottom: 0,
            fixedElements: null,
            responsive: 0,
            responsiveWidth: 0,
            responsiveHeight: 0,
            responsiveSlides: !1,
            parallax: !1,
            parallaxOptions: {
                type: "reveal",
                percentage: 62,
                property: "translate"
            },
            sectionSelector: g,
            slideSelector: I,
            afterLoad: null,
            onLeave: null,
            afterRender: null,
            afterResize: null,
            afterReBuild: null,
            afterSlideLoad: null,
            onSlideLeave: null,
            afterResponsive: null,
            lazyLoading: !0
        }, B);
        var ln, cn, un, dn, hn = !1,
            fn = navigator.userAgent.match(/(iPhone|iPod|iPad|Android|playbook|silk|BlackBerry|BB10|Windows Phone|Tizen|Bada|webOS|IEMobile|Opera Mini)/),
            pn = "ontouchstart" in t || navigator.msMaxTouchPoints > 0 || navigator.maxTouchPoints,
            mn = e(this),
            gn = Z.height(),
            vn = !1,
            yn = !0,
            bn = !0,
            wn = [],
            Tn = {};
        Tn.m = {
            up: !0,
            down: !0,
            left: !0,
            right: !0
        }, Tn.k = e.extend(!0, {}, Tn.m);
        var xn, Cn, Dn, kn, Sn, En, Mn, Nn = Yt(),
            An = {
                touchmove: "ontouchmove" in t ? "touchmove" : Nn.move,
                touchstart: "ontouchstart" in t ? "touchstart" : Nn.down
            },
            In = e.extend(!0, {}, B);
        nn(), e.extend(e.easing, {
            easeInOutCubic: function(e, t, n, i, o) {
                return (t /= o / 2) < 1 ? i / 2 * t * t * t + n : i / 2 * ((t -= 2) * t * t + 2) + n
            }
        }), e(this).length && (an.version = "2.9.5", an.setAutoScrolling = z, an.setRecordHistory = X, an.setScrollingSpeed = J, an.setFitToSection = te, an.setLockAnchors = ne, an.setMouseWheelScrolling = ie, an.setAllowScrolling = oe, an.setKeyboardScrolling = se, an.moveSectionUp = re, an.moveSectionDown = ae, an.silentMoveTo = le, an.moveTo = ce, an.moveSlideRight = ue, an.moveSlideLeft = de, an.fitToSection = Ae, an.reBuild = he, an.setResponsive = fe, an.destroy = Zt, an.shared = {
            afterRenderActions: Ee
        }, pe(), me());
        var On = !1,
            $n = 0,
            Un = 0,
            jn = 0,
            Ln = 0,
            Hn = 0,
            Fn = (new Date).getTime(),
            Rn = 0,
            Pn = 0,
            _n = gn
    }
}), ! function(e, t) {
    "function" == typeof define && define.amd ? define("ev-emitter/ev-emitter", t) : "object" == typeof module && module.exports ? module.exports = t() : e.EvEmitter = t()
}("undefined" != typeof window ? window : this, function() {
    function e() {}
    var t = e.prototype;
    return t.on = function(e, t) {
        if (e && t) {
            var n = this._events = this._events || {},
                i = n[e] = n[e] || [];
            return -1 == i.indexOf(t) && i.push(t), this
        }
    }, t.once = function(e, t) {
        if (e && t) {
            this.on(e, t);
            var n = this._onceEvents = this._onceEvents || {},
                i = n[e] = n[e] || {};
            return i[t] = !0, this
        }
    }, t.off = function(e, t) {
        var n = this._events && this._events[e];
        if (n && n.length) {
            var i = n.indexOf(t);
            return -1 != i && n.splice(i, 1), this
        }
    }, t.emitEvent = function(e, t) {
        var n = this._events && this._events[e];
        if (n && n.length) {
            var i = 0,
                o = n[i];
            t = t || [];
            for (var s = this._onceEvents && this._onceEvents[e]; o;) {
                var r = s && s[o];
                r && (this.off(e, o), delete s[o]), o.apply(this, t), i += r ? 0 : 1, o = n[i]
            }
            return this
        }
    }, t.allOff = t.removeAllListeners = function() {
        delete this._events, delete this._onceEvents
    }, e
}), function(e, t) {
    "use strict";
    "function" == typeof define && define.amd ? define(["ev-emitter/ev-emitter"], function(n) {
        return t(e, n)
    }) : "object" == typeof module && module.exports ? module.exports = t(e, require("ev-emitter")) : e.imagesLoaded = t(e, e.EvEmitter)
}("undefined" != typeof window ? window : this, function(e, t) {
    function n(e, t) {
        for (var n in t) e[n] = t[n];
        return e
    }

    function i(e) {
        var t = [];
        if (Array.isArray(e)) t = e;
        else if ("number" == typeof e.length)
            for (var n = 0; n < e.length; n++) t.push(e[n]);
        else t.push(e);
        return t
    }

    function o(e, t, s) {
        return this instanceof o ? ("string" == typeof e && (e = document.querySelectorAll(e)), this.elements = i(e), this.options = n({}, this.options), "function" == typeof t ? s = t : n(this.options, t), s && this.on("always", s), this.getImages(), a && (this.jqDeferred = new a.Deferred), void setTimeout(function() {
            this.check()
        }.bind(this))) : new o(e, t, s)
    }

    function s(e) {
        this.img = e
    }

    function r(e, t) {
        this.url = e, this.element = t, this.img = new Image
    }
    var a = e.jQuery,
        l = e.console;
    o.prototype = Object.create(t.prototype), o.prototype.options = {}, o.prototype.getImages = function() {
        this.images = [], this.elements.forEach(this.addElementImages, this)
    }, o.prototype.addElementImages = function(e) {
        "IMG" == e.nodeName && this.addImage(e), this.options.background === !0 && this.addElementBackgroundImages(e);
        var t = e.nodeType;
        if (t && c[t]) {
            for (var n = e.querySelectorAll("img"), i = 0; i < n.length; i++) {
                var o = n[i];
                this.addImage(o)
            }
            if ("string" == typeof this.options.background) {
                var s = e.querySelectorAll(this.options.background);
                for (i = 0; i < s.length; i++) {
                    var r = s[i];
                    this.addElementBackgroundImages(r)
                }
            }
        }
    };
    var c = {
        1: !0,
        9: !0,
        11: !0
    };
    return o.prototype.addElementBackgroundImages = function(e) {
        var t = getComputedStyle(e);
        if (t)
            for (var n = /url\((['"])?(.*?)\1\)/gi, i = n.exec(t.backgroundImage); null !== i;) {
                var o = i && i[2];
                o && this.addBackground(o, e), i = n.exec(t.backgroundImage)
            }
    }, o.prototype.addImage = function(e) {
        var t = new s(e);
        this.images.push(t)
    }, o.prototype.addBackground = function(e, t) {
        var n = new r(e, t);
        this.images.push(n)
    }, o.prototype.check = function() {
        function e(e, n, i) {
            setTimeout(function() {
                t.progress(e, n, i)
            })
        }
        var t = this;
        return this.progressedCount = 0, this.hasAnyBroken = !1, this.images.length ? void this.images.forEach(function(t) {
            t.once("progress", e), t.check()
        }) : void this.complete()
    }, o.prototype.progress = function(e, t, n) {
        this.progressedCount++, this.hasAnyBroken = this.hasAnyBroken || !e.isLoaded, this.emitEvent("progress", [this, e, t]), this.jqDeferred && this.jqDeferred.notify && this.jqDeferred.notify(this, e), this.progressedCount == this.images.length && this.complete(), this.options.debug && l && l.log("progress: " + n, e, t)
    }, o.prototype.complete = function() {
        var e = this.hasAnyBroken ? "fail" : "done";
        if (this.isComplete = !0, this.emitEvent(e, [this]), this.emitEvent("always", [this]), this.jqDeferred) {
            var t = this.hasAnyBroken ? "reject" : "resolve";
            this.jqDeferred[t](this)
        }
    }, s.prototype = Object.create(t.prototype), s.prototype.check = function() {
        var e = this.getIsImageComplete();
        return e ? void this.confirm(0 !== this.img.naturalWidth, "naturalWidth") : (this.proxyImage = new Image, this.proxyImage.addEventListener("load", this), this.proxyImage.addEventListener("error", this), this.img.addEventListener("load", this), this.img.addEventListener("error", this), void(this.proxyImage.src = this.img.src))
    }, s.prototype.getIsImageComplete = function() {
        return this.img.complete && void 0 !== this.img.naturalWidth
    }, s.prototype.confirm = function(e, t) {
        this.isLoaded = e, this.emitEvent("progress", [this, this.img, t])
    }, s.prototype.handleEvent = function(e) {
        var t = "on" + e.type;
        this[t] && this[t](e)
    }, s.prototype.onload = function() {
        this.confirm(!0, "onload"), this.unbindEvents()
    }, s.prototype.onerror = function() {
        this.confirm(!1, "onerror"), this.unbindEvents()
    }, s.prototype.unbindEvents = function() {
        this.proxyImage.removeEventListener("load", this), this.proxyImage.removeEventListener("error", this), this.img.removeEventListener("load", this), this.img.removeEventListener("error", this)
    }, r.prototype = Object.create(s.prototype), r.prototype.check = function() {
        this.img.addEventListener("load", this), this.img.addEventListener("error", this), this.img.src = this.url;
        var e = this.getIsImageComplete();
        e && (this.confirm(0 !== this.img.naturalWidth, "naturalWidth"), this.unbindEvents())
    }, r.prototype.unbindEvents = function() {
        this.img.removeEventListener("load", this), this.img.removeEventListener("error", this)
    }, r.prototype.confirm = function(e, t) {
        this.isLoaded = e, this.emitEvent("progress", [this, this.element, t])
    }, o.makeJQueryPlugin = function(t) {
        t = t || e.jQuery, t && (a = t, a.fn.imagesLoaded = function(e, t) {
            var n = new o(this, e, t);
            return n.jqDeferred.promise(a(this))
        })
    }, o.makeJQueryPlugin(), o
}), "undefined" == typeof jQuery) throw new Error("Bootstrap's JavaScript requires jQuery"); + function(e) {
    "use strict";
    var t = e.fn.jquery.split(" ")[0].split(".");
    if (t[0] < 2 && t[1] < 9 || 1 == t[0] && 9 == t[1] && t[2] < 1 || t[0] > 3) throw new Error("Bootstrap's JavaScript requires jQuery version 1.9.1 or higher, but lower than version 4")
}(jQuery), + function(e) {
    "use strict";

    function t() {
        var e = document.createElement("bootstrap"),
            t = {
                WebkitTransition: "webkitTransitionEnd",
                MozTransition: "transitionend",
                OTransition: "oTransitionEnd otransitionend",
                transition: "transitionend"
            };
        for (var n in t)
            if (void 0 !== e.style[n]) return {
                end: t[n]
            };
        return !1
    }
    e.fn.emulateTransitionEnd = function(t) {
        var n = !1,
            i = this;
        e(this).one("bsTransitionEnd", function() {
            n = !0
        });
        var o = function() {
            n || e(i).trigger(e.support.transition.end)
        };
        return setTimeout(o, t), this
    }, e(function() {
        e.support.transition = t(), e.support.transition && (e.event.special.bsTransitionEnd = {
            bindType: e.support.transition.end,
            delegateType: e.support.transition.end,
            handle: function(t) {
                if (e(t.target).is(this)) return t.handleObj.handler.apply(this, arguments)
            }
        })
    })
}(jQuery), + function(e) {
    "use strict";

    function t(t) {
        return this.each(function() {
            var n = e(this),
                o = n.data("bs.alert");
            o || n.data("bs.alert", o = new i(this)), "string" == typeof t && o[t].call(n)
        })
    }
    var n = '[data-dismiss="alert"]',
        i = function(t) {
            e(t).on("click", n, this.close)
        };
    i.VERSION = "3.3.7", i.TRANSITION_DURATION = 150, i.prototype.close = function(t) {
        function n() {
            r.detach().trigger("closed.bs.alert").remove()
        }
        var o = e(this),
            s = o.attr("data-target");
        s || (s = o.attr("href"), s = s && s.replace(/.*(?=#[^\s]*$)/, ""));
        var r = e("#" === s ? [] : s);
        t && t.preventDefault(), r.length || (r = o.closest(".alert")), r.trigger(t = e.Event("close.bs.alert")), t.isDefaultPrevented() || (r.removeClass("in"), e.support.transition && r.hasClass("fade") ? r.one("bsTransitionEnd", n).emulateTransitionEnd(i.TRANSITION_DURATION) : n())
    };
    var o = e.fn.alert;
    e.fn.alert = t, e.fn.alert.Constructor = i, e.fn.alert.noConflict = function() {
        return e.fn.alert = o, this
    }, e(document).on("click.bs.alert.data-api", n, i.prototype.close)
}(jQuery), + function(e) {
    "use strict";

    function t(t) {
        return this.each(function() {
            var i = e(this),
                o = i.data("bs.button"),
                s = "object" == typeof t && t;
            o || i.data("bs.button", o = new n(this, s)), "toggle" == t ? o.toggle() : t && o.setState(t)
        })
    }
    var n = function(t, i) {
        this.$element = e(t), this.options = e.extend({}, n.DEFAULTS, i), this.isLoading = !1
    };
    n.VERSION = "3.3.7", n.DEFAULTS = {
        loadingText: "loading..."
    }, n.prototype.setState = function(t) {
        var n = "disabled",
            i = this.$element,
            o = i.is("input") ? "val" : "html",
            s = i.data();
        t += "Text", null == s.resetText && i.data("resetText", i[o]()), setTimeout(e.proxy(function() {
            i[o](null == s[t] ? this.options[t] : s[t]), "loadingText" == t ? (this.isLoading = !0, i.addClass(n).attr(n, n).prop(n, !0)) : this.isLoading && (this.isLoading = !1, i.removeClass(n).removeAttr(n).prop(n, !1))
        }, this), 0)
    }, n.prototype.toggle = function() {
        var e = !0,
            t = this.$element.closest('[data-toggle="buttons"]');
        if (t.length) {
            var n = this.$element.find("input");
            "radio" == n.prop("type") ? (n.prop("checked") && (e = !1), t.find(".active").removeClass("active"), this.$element.addClass("active")) : "checkbox" == n.prop("type") && (n.prop("checked") !== this.$element.hasClass("active") && (e = !1), this.$element.toggleClass("active")), n.prop("checked", this.$element.hasClass("active")), e && n.trigger("change")
        } else this.$element.attr("aria-pressed", !this.$element.hasClass("active")), this.$element.toggleClass("active")
    };
    var i = e.fn.button;
    e.fn.button = t, e.fn.button.Constructor = n, e.fn.button.noConflict = function() {
        return e.fn.button = i, this
    }, e(document).on("click.bs.button.data-api", '[data-toggle^="button"]', function(n) {
        var i = e(n.target).closest(".btn");
        t.call(i, "toggle"), e(n.target).is('input[type="radio"], input[type="checkbox"]') || (n.preventDefault(), i.is("input,button") ? i.trigger("focus") : i.find("input:visible,button:visible").first().trigger("focus"))
    }).on("focus.bs.button.data-api blur.bs.button.data-api", '[data-toggle^="button"]', function(t) {
        e(t.target).closest(".btn").toggleClass("focus", /^focus(in)?$/.test(t.type))
    })
}(jQuery), + function(e) {
    "use strict";

    function t(t) {
        return this.each(function() {
            var i = e(this),
                o = i.data("bs.carousel"),
                s = e.extend({}, n.DEFAULTS, i.data(), "object" == typeof t && t),
                r = "string" == typeof t ? t : s.slide;
            o || i.data("bs.carousel", o = new n(this, s)), "number" == typeof t ? o.to(t) : r ? o[r]() : s.interval && o.pause().cycle()
        })
    }
    var n = function(t, n) {
        this.$element = e(t), this.$indicators = this.$element.find(".carousel-indicators"), this.options = n, this.paused = null, this.sliding = null, this.interval = null, this.$active = null, this.$items = null, this.options.keyboard && this.$element.on("keydown.bs.carousel", e.proxy(this.keydown, this)), "hover" == this.options.pause && !("ontouchstart" in document.documentElement) && this.$element.on("mouseenter.bs.carousel", e.proxy(this.pause, this)).on("mouseleave.bs.carousel", e.proxy(this.cycle, this))
    };
    n.VERSION = "3.3.7", n.TRANSITION_DURATION = 600, n.DEFAULTS = {
        interval: 5e3,
        pause: "hover",
        wrap: !0,
        keyboard: !0
    }, n.prototype.keydown = function(e) {
        if (!/input|textarea/i.test(e.target.tagName)) {
            switch (e.which) {
                case 37:
                    this.prev();
                    break;
                case 39:
                    this.next();
                    break;
                default:
                    return
            }
            e.preventDefault()
        }
    }, n.prototype.cycle = function(t) {
        return t || (this.paused = !1), this.interval && clearInterval(this.interval), this.options.interval && !this.paused && (this.interval = setInterval(e.proxy(this.next, this), this.options.interval)), this
    }, n.prototype.getItemIndex = function(e) {
        return this.$items = e.parent().children(".item"), this.$items.index(e || this.$active)
    }, n.prototype.getItemForDirection = function(e, t) {
        var n = this.getItemIndex(t),
            i = "prev" == e && 0 === n || "next" == e && n == this.$items.length - 1;
        if (i && !this.options.wrap) return t;
        var o = "prev" == e ? -1 : 1,
            s = (n + o) % this.$items.length;
        return this.$items.eq(s)
    }, n.prototype.to = function(e) {
        var t = this,
            n = this.getItemIndex(this.$active = this.$element.find(".item.active"));
        if (!(e > this.$items.length - 1 || e < 0)) return this.sliding ? this.$element.one("slid.bs.carousel", function() {
            t.to(e)
        }) : n == e ? this.pause().cycle() : this.slide(e > n ? "next" : "prev", this.$items.eq(e))
    }, n.prototype.pause = function(t) {
        return t || (this.paused = !0), this.$element.find(".next, .prev").length && e.support.transition && (this.$element.trigger(e.support.transition.end), this.cycle(!0)), this.interval = clearInterval(this.interval), this
    }, n.prototype.next = function() {
        if (!this.sliding) return this.slide("next")
    }, n.prototype.prev = function() {
        if (!this.sliding) return this.slide("prev")
    }, n.prototype.slide = function(t, i) {
        var o = this.$element.find(".item.active"),
            s = i || this.getItemForDirection(t, o),
            r = this.interval,
            a = "next" == t ? "left" : "right",
            l = this;
        if (s.hasClass("active")) return this.sliding = !1;
        var c = s[0],
            u = e.Event("slide.bs.carousel", {
                relatedTarget: c,
                direction: a
            });
        if (this.$element.trigger(u), !u.isDefaultPrevented()) {
            if (this.sliding = !0, r && this.pause(), this.$indicators.length) {
                this.$indicators.find(".active").removeClass("active");
                var d = e(this.$indicators.children()[this.getItemIndex(s)]);
                d && d.addClass("active")
            }
            var h = e.Event("slid.bs.carousel", {
                relatedTarget: c,
                direction: a
            });
            return e.support.transition && this.$element.hasClass("slide") ? (s.addClass(t), s[0].offsetWidth, o.addClass(a), s.addClass(a), o.one("bsTransitionEnd", function() {
                s.removeClass([t, a].join(" ")).addClass("active"), o.removeClass(["active", a].join(" ")), l.sliding = !1, setTimeout(function() {
                    l.$element.trigger(h)
                }, 0)
            }).emulateTransitionEnd(n.TRANSITION_DURATION)) : (o.removeClass("active"), s.addClass("active"), this.sliding = !1, this.$element.trigger(h)), r && this.cycle(), this
        }
    };
    var i = e.fn.carousel;
    e.fn.carousel = t, e.fn.carousel.Constructor = n, e.fn.carousel.noConflict = function() {
        return e.fn.carousel = i, this
    };
    var o = function(n) {
        var i, o = e(this),
            s = e(o.attr("data-target") || (i = o.attr("href")) && i.replace(/.*(?=#[^\s]+$)/, ""));
        if (s.hasClass("carousel")) {
            var r = e.extend({}, s.data(), o.data()),
                a = o.attr("data-slide-to");
            a && (r.interval = !1), t.call(s, r), a && s.data("bs.carousel").to(a), n.preventDefault()
        }
    };
    e(document).on("click.bs.carousel.data-api", "[data-slide]", o).on("click.bs.carousel.data-api", "[data-slide-to]", o), e(window).on("load", function() {
        e('[data-ride="carousel"]').each(function() {
            var n = e(this);
            t.call(n, n.data())
        })
    })
}(jQuery), + function(e) {
    "use strict";

    function t(t) {
        var n, i = t.attr("data-target") || (n = t.attr("href")) && n.replace(/.*(?=#[^\s]+$)/, "");
        return e(i)
    }

    function n(t) {
        return this.each(function() {
            var n = e(this),
                o = n.data("bs.collapse"),
                s = e.extend({}, i.DEFAULTS, n.data(), "object" == typeof t && t);
            !o && s.toggle && /show|hide/.test(t) && (s.toggle = !1), o || n.data("bs.collapse", o = new i(this, s)), "string" == typeof t && o[t]()
        })
    }
    var i = function(t, n) {
        this.$element = e(t), this.options = e.extend({}, i.DEFAULTS, n), this.$trigger = e('[data-toggle="collapse"][href="#' + t.id + '"],[data-toggle="collapse"][data-target="#' + t.id + '"]'), this.transitioning = null, this.options.parent ? this.$parent = this.getParent() : this.addAriaAndCollapsedClass(this.$element, this.$trigger), this.options.toggle && this.toggle()
    };
    i.VERSION = "3.3.7", i.TRANSITION_DURATION = 350, i.DEFAULTS = {
        toggle: !0
    }, i.prototype.dimension = function() {
        var e = this.$element.hasClass("width");
        return e ? "width" : "height"
    }, i.prototype.show = function() {
        if (!this.transitioning && !this.$element.hasClass("in")) {
            var t, o = this.$parent && this.$parent.children(".panel").children(".in, .collapsing");
            if (!(o && o.length && (t = o.data("bs.collapse"), t && t.transitioning))) {
                var s = e.Event("show.bs.collapse");
                if (this.$element.trigger(s), !s.isDefaultPrevented()) {
                    o && o.length && (n.call(o, "hide"), t || o.data("bs.collapse", null));
                    var r = this.dimension();
                    this.$element.removeClass("collapse").addClass("collapsing")[r](0).attr("aria-expanded", !0), this.$trigger.removeClass("collapsed").attr("aria-expanded", !0), this.transitioning = 1;
                    var a = function() {
                        this.$element.removeClass("collapsing").addClass("collapse in")[r](""), this.transitioning = 0, this.$element.trigger("shown.bs.collapse")
                    };
                    if (!e.support.transition) return a.call(this);
                    var l = e.camelCase(["scroll", r].join("-"));
                    this.$element.one("bsTransitionEnd", e.proxy(a, this)).emulateTransitionEnd(i.TRANSITION_DURATION)[r](this.$element[0][l])
                }
            }
        }
    }, i.prototype.hide = function() {
        if (!this.transitioning && this.$element.hasClass("in")) {
            var t = e.Event("hide.bs.collapse");
            if (this.$element.trigger(t), !t.isDefaultPrevented()) {
                var n = this.dimension();
                this.$element[n](this.$element[n]())[0].offsetHeight, this.$element.addClass("collapsing").removeClass("collapse in").attr("aria-expanded", !1), this.$trigger.addClass("collapsed").attr("aria-expanded", !1), this.transitioning = 1;
                var o = function() {
                    this.transitioning = 0, this.$element.removeClass("collapsing").addClass("collapse").trigger("hidden.bs.collapse")
                };
                return e.support.transition ? void this.$element[n](0).one("bsTransitionEnd", e.proxy(o, this)).emulateTransitionEnd(i.TRANSITION_DURATION) : o.call(this)
            }
        }
    }, i.prototype.toggle = function() {
        this[this.$element.hasClass("in") ? "hide" : "show"]()
    }, i.prototype.getParent = function() {
        return e(this.options.parent).find('[data-toggle="collapse"][data-parent="' + this.options.parent + '"]').each(e.proxy(function(n, i) {
            var o = e(i);
            this.addAriaAndCollapsedClass(t(o), o)
        }, this)).end()
    }, i.prototype.addAriaAndCollapsedClass = function(e, t) {
        var n = e.hasClass("in");
        e.attr("aria-expanded", n), t.toggleClass("collapsed", !n).attr("aria-expanded", n)
    };
    var o = e.fn.collapse;
    e.fn.collapse = n, e.fn.collapse.Constructor = i, e.fn.collapse.noConflict = function() {
        return e.fn.collapse = o, this
    }, e(document).on("click.bs.collapse.data-api", '[data-toggle="collapse"]', function(i) {
        var o = e(this);
        o.attr("data-target") || i.preventDefault();
        var s = t(o),
            r = s.data("bs.collapse"),
            a = r ? "toggle" : o.data();
        n.call(s, a)
    })
}(jQuery), + function(e) {
    "use strict";

    function t(t) {
        var n = t.attr("data-target");
        n || (n = t.attr("href"), n = n && /#[A-Za-z]/.test(n) && n.replace(/.*(?=#[^\s]*$)/, ""));
        var i = n && e(n);
        return i && i.length ? i : t.parent()
    }

    function n(n) {
        n && 3 === n.which || (e(o).remove(), e(s).each(function() {
            var i = e(this),
                o = t(i),
                s = {
                    relatedTarget: this
                };
            o.hasClass("open") && (n && "click" == n.type && /input|textarea/i.test(n.target.tagName) && e.contains(o[0], n.target) || (o.trigger(n = e.Event("hide.bs.dropdown", s)), n.isDefaultPrevented() || (i.attr("aria-expanded", "false"), o.removeClass("open").trigger(e.Event("hidden.bs.dropdown", s)))))
        }))
    }

    function i(t) {
        return this.each(function() {
            var n = e(this),
                i = n.data("bs.dropdown");
            i || n.data("bs.dropdown", i = new r(this)), "string" == typeof t && i[t].call(n)
        })
    }
    var o = ".dropdown-backdrop",
        s = '[data-toggle="dropdown"]',
        r = function(t) {
            e(t).on("click.bs.dropdown", this.toggle)
        };
    r.VERSION = "3.3.7", r.prototype.toggle = function(i) {
        var o = e(this);
        if (!o.is(".disabled, :disabled")) {
            var s = t(o),
                r = s.hasClass("open");
            if (n(), !r) {
                "ontouchstart" in document.documentElement && !s.closest(".navbar-nav").length && e(document.createElement("div")).addClass("dropdown-backdrop").insertAfter(e(this)).on("click", n);
                var a = {
                    relatedTarget: this
                };
                if (s.trigger(i = e.Event("show.bs.dropdown", a)), i.isDefaultPrevented()) return;
                o.trigger("focus").attr("aria-expanded", "true"), s.toggleClass("open").trigger(e.Event("shown.bs.dropdown", a))
            }
            return !1
        }
    }, r.prototype.keydown = function(n) {
        if (/(38|40|27|32)/.test(n.which) && !/input|textarea/i.test(n.target.tagName)) {
            var i = e(this);
            if (n.preventDefault(), n.stopPropagation(), !i.is(".disabled, :disabled")) {
                var o = t(i),
                    r = o.hasClass("open");
                if (!r && 27 != n.which || r && 27 == n.which) return 27 == n.which && o.find(s).trigger("focus"), i.trigger("click");
                var a = " li:not(.disabled):visible a",
                    l = o.find(".dropdown-menu" + a);
                if (l.length) {
                    var c = l.index(n.target);
                    38 == n.which && c > 0 && c--, 40 == n.which && c < l.length - 1 && c++, ~c || (c = 0), l.eq(c).trigger("focus")
                }
            }
        }
    };
    var a = e.fn.dropdown;
    e.fn.dropdown = i, e.fn.dropdown.Constructor = r, e.fn.dropdown.noConflict = function() {
        return e.fn.dropdown = a, this
    }, e(document).on("click.bs.dropdown.data-api", n).on("click.bs.dropdown.data-api", ".dropdown form", function(e) {
        e.stopPropagation()
    }).on("click.bs.dropdown.data-api", s, r.prototype.toggle).on("keydown.bs.dropdown.data-api", s, r.prototype.keydown).on("keydown.bs.dropdown.data-api", ".dropdown-menu", r.prototype.keydown)
}(jQuery), + function(e) {
    "use strict";

    function t(t, i) {
        return this.each(function() {
            var o = e(this),
                s = o.data("bs.modal"),
                r = e.extend({}, n.DEFAULTS, o.data(), "object" == typeof t && t);
            s || o.data("bs.modal", s = new n(this, r)), "string" == typeof t ? s[t](i) : r.show && s.show(i)
        })
    }
    var n = function(t, n) {
        this.options = n, this.$body = e(document.body), this.$element = e(t), this.$dialog = this.$element.find(".modal-dialog"), this.$backdrop = null, this.isShown = null, this.originalBodyPad = null, this.scrollbarWidth = 0, this.ignoreBackdropClick = !1, this.options.remote && this.$element.find(".modal-content").load(this.options.remote, e.proxy(function() {
            this.$element.trigger("loaded.bs.modal")
        }, this))
    };
    n.VERSION = "3.3.7", n.TRANSITION_DURATION = 300, n.BACKDROP_TRANSITION_DURATION = 150, n.DEFAULTS = {
        backdrop: !0,
        keyboard: !0,
        show: !0
    }, n.prototype.toggle = function(e) {
        return this.isShown ? this.hide() : this.show(e)
    }, n.prototype.show = function(t) {
        var i = this,
            o = e.Event("show.bs.modal", {
                relatedTarget: t
            });
        this.$element.trigger(o), this.isShown || o.isDefaultPrevented() || (this.isShown = !0, this.checkScrollbar(), this.setScrollbar(), this.$body.addClass("modal-open"), this.escape(), this.resize(), this.$element.on("click.dismiss.bs.modal", '[data-dismiss="modal"]', e.proxy(this.hide, this)), this.$dialog.on("mousedown.dismiss.bs.modal", function() {
            i.$element.one("mouseup.dismiss.bs.modal", function(t) {
                e(t.target).is(i.$element) && (i.ignoreBackdropClick = !0)
            })
        }), this.backdrop(function() {
            var o = e.support.transition && i.$element.hasClass("fade");
            i.$element.parent().length || i.$element.appendTo(i.$body), i.$element.show().scrollTop(0), i.adjustDialog(), o && i.$element[0].offsetWidth, i.$element.addClass("in"), i.enforceFocus();
            var s = e.Event("shown.bs.modal", {
                relatedTarget: t
            });
            o ? i.$dialog.one("bsTransitionEnd", function() {
                i.$element.trigger("focus").trigger(s)
            }).emulateTransitionEnd(n.TRANSITION_DURATION) : i.$element.trigger("focus").trigger(s)
        }))
    }, n.prototype.hide = function(t) {
        t && t.preventDefault(), t = e.Event("hide.bs.modal"), this.$element.trigger(t), this.isShown && !t.isDefaultPrevented() && (this.isShown = !1, this.escape(), this.resize(), e(document).off("focusin.bs.modal"), this.$element.removeClass("in").off("click.dismiss.bs.modal").off("mouseup.dismiss.bs.modal"), this.$dialog.off("mousedown.dismiss.bs.modal"), e.support.transition && this.$element.hasClass("fade") ? this.$element.one("bsTransitionEnd", e.proxy(this.hideModal, this)).emulateTransitionEnd(n.TRANSITION_DURATION) : this.hideModal())
    }, n.prototype.enforceFocus = function() {
        e(document).off("focusin.bs.modal").on("focusin.bs.modal", e.proxy(function(e) {
            document === e.target || this.$element[0] === e.target || this.$element.has(e.target).length || this.$element.trigger("focus")
        }, this))
    }, n.prototype.escape = function() {
        this.isShown && this.options.keyboard ? this.$element.on("keydown.dismiss.bs.modal", e.proxy(function(e) {
            27 == e.which && this.hide()
        }, this)) : this.isShown || this.$element.off("keydown.dismiss.bs.modal")
    }, n.prototype.resize = function() {
        this.isShown ? e(window).on("resize.bs.modal", e.proxy(this.handleUpdate, this)) : e(window).off("resize.bs.modal")
    }, n.prototype.hideModal = function() {
        var e = this;
        this.$element.hide(), this.backdrop(function() {
            e.$body.removeClass("modal-open"), e.resetAdjustments(), e.resetScrollbar(), e.$element.trigger("hidden.bs.modal")
        })
    }, n.prototype.removeBackdrop = function() {
        this.$backdrop && this.$backdrop.remove(), this.$backdrop = null
    }, n.prototype.backdrop = function(t) {
        var i = this,
            o = this.$element.hasClass("fade") ? "fade" : "";
        if (this.isShown && this.options.backdrop) {
            var s = e.support.transition && o;
            if (this.$backdrop = e(document.createElement("div")).addClass("modal-backdrop " + o).appendTo(this.$body), this.$element.on("click.dismiss.bs.modal", e.proxy(function(e) {
                return this.ignoreBackdropClick ? void(this.ignoreBackdropClick = !1) : void(e.target === e.currentTarget && ("static" == this.options.backdrop ? this.$element[0].focus() : this.hide()))
            }, this)), s && this.$backdrop[0].offsetWidth, this.$backdrop.addClass("in"), !t) return;
            s ? this.$backdrop.one("bsTransitionEnd", t).emulateTransitionEnd(n.BACKDROP_TRANSITION_DURATION) : t()
        } else if (!this.isShown && this.$backdrop) {
            this.$backdrop.removeClass("in");
            var r = function() {
                i.removeBackdrop(), t && t()
            };
            e.support.transition && this.$element.hasClass("fade") ? this.$backdrop.one("bsTransitionEnd", r).emulateTransitionEnd(n.BACKDROP_TRANSITION_DURATION) : r()
        } else t && t()
    }, n.prototype.handleUpdate = function() {
        this.adjustDialog()
    }, n.prototype.adjustDialog = function() {
        var e = this.$element[0].scrollHeight > document.documentElement.clientHeight;
        this.$element.css({
            paddingLeft: !this.bodyIsOverflowing && e ? this.scrollbarWidth : "",
            paddingRight: this.bodyIsOverflowing && !e ? this.scrollbarWidth : ""
        })
    }, n.prototype.resetAdjustments = function() {
        this.$element.css({
            paddingLeft: "",
            paddingRight: ""
        })
    }, n.prototype.checkScrollbar = function() {
        var e = window.innerWidth;
        if (!e) {
            var t = document.documentElement.getBoundingClientRect();
            e = t.right - Math.abs(t.left)
        }
        this.bodyIsOverflowing = document.body.clientWidth < e, this.scrollbarWidth = this.measureScrollbar()
    }, n.prototype.setScrollbar = function() {
        var e = parseInt(this.$body.css("padding-right") || 0, 10);
        this.originalBodyPad = document.body.style.paddingRight || "", this.bodyIsOverflowing && this.$body.css("padding-right", e + this.scrollbarWidth)
    }, n.prototype.resetScrollbar = function() {
        this.$body.css("padding-right", this.originalBodyPad)
    }, n.prototype.measureScrollbar = function() {
        var e = document.createElement("div");
        e.className = "modal-scrollbar-measure", this.$body.append(e);
        var t = e.offsetWidth - e.clientWidth;
        return this.$body[0].removeChild(e), t
    };
    var i = e.fn.modal;
    e.fn.modal = t, e.fn.modal.Constructor = n, e.fn.modal.noConflict = function() {
        return e.fn.modal = i, this
    }, e(document).on("click.bs.modal.data-api", '[data-toggle="modal"]', function(n) {
        var i = e(this),
            o = i.attr("href"),
            s = e(i.attr("data-target") || o && o.replace(/.*(?=#[^\s]+$)/, "")),
            r = s.data("bs.modal") ? "toggle" : e.extend({
                remote: !/#/.test(o) && o
            }, s.data(), i.data());
        i.is("a") && n.preventDefault(), s.one("show.bs.modal", function(e) {
            e.isDefaultPrevented() || s.one("hidden.bs.modal", function() {
                i.is(":visible") && i.trigger("focus")
            })
        }), t.call(s, r, this)
    })
}(jQuery), + function(e) {
    "use strict";

    function t(t) {
        return this.each(function() {
            var i = e(this),
                o = i.data("bs.tooltip"),
                s = "object" == typeof t && t;
            !o && /destroy|hide/.test(t) || (o || i.data("bs.tooltip", o = new n(this, s)), "string" == typeof t && o[t]())
        })
    }
    var n = function(e, t) {
        this.type = null, this.options = null, this.enabled = null, this.timeout = null, this.hoverState = null, this.$element = null, this.inState = null, this.init("tooltip", e, t)
    };
    n.VERSION = "3.3.7", n.TRANSITION_DURATION = 150, n.DEFAULTS = {
        animation: !0,
        placement: "top",
        selector: !1,
        template: '<div class="tooltip" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>',
        trigger: "hover focus",
        title: "",
        delay: 0,
        html: !1,
        container: !1,
        viewport: {
            selector: "body",
            padding: 0
        }
    }, n.prototype.init = function(t, n, i) {
        if (this.enabled = !0, this.type = t, this.$element = e(n), this.options = this.getOptions(i), this.$viewport = this.options.viewport && e(e.isFunction(this.options.viewport) ? this.options.viewport.call(this, this.$element) : this.options.viewport.selector || this.options.viewport), this.inState = {
            click: !1,
            hover: !1,
            focus: !1
        }, this.$element[0] instanceof document.constructor && !this.options.selector) throw new Error("`selector` option must be specified when initializing " + this.type + " on the window.document object!");
        for (var o = this.options.trigger.split(" "), s = o.length; s--;) {
            var r = o[s];
            if ("click" == r) this.$element.on("click." + this.type, this.options.selector, e.proxy(this.toggle, this));
            else if ("manual" != r) {
                var a = "hover" == r ? "mouseenter" : "focusin",
                    l = "hover" == r ? "mouseleave" : "focusout";
                this.$element.on(a + "." + this.type, this.options.selector, e.proxy(this.enter, this)), this.$element.on(l + "." + this.type, this.options.selector, e.proxy(this.leave, this))
            }
        }
        this.options.selector ? this._options = e.extend({}, this.options, {
            trigger: "manual",
            selector: ""
        }) : this.fixTitle()
    }, n.prototype.getDefaults = function() {
        return n.DEFAULTS
    }, n.prototype.getOptions = function(t) {
        return t = e.extend({}, this.getDefaults(), this.$element.data(), t), t.delay && "number" == typeof t.delay && (t.delay = {
            show: t.delay,
            hide: t.delay
        }), t
    }, n.prototype.getDelegateOptions = function() {
        var t = {},
            n = this.getDefaults();
        return this._options && e.each(this._options, function(e, i) {
            n[e] != i && (t[e] = i)
        }), t
    }, n.prototype.enter = function(t) {
        var n = t instanceof this.constructor ? t : e(t.currentTarget).data("bs." + this.type);
        return n || (n = new this.constructor(t.currentTarget, this.getDelegateOptions()), e(t.currentTarget).data("bs." + this.type, n)), t instanceof e.Event && (n.inState["focusin" == t.type ? "focus" : "hover"] = !0), n.tip().hasClass("in") || "in" == n.hoverState ? void(n.hoverState = "in") : (clearTimeout(n.timeout), n.hoverState = "in", n.options.delay && n.options.delay.show ? void(n.timeout = setTimeout(function() {
            "in" == n.hoverState && n.show()
        }, n.options.delay.show)) : n.show())
    }, n.prototype.isInStateTrue = function() {
        for (var e in this.inState)
            if (this.inState[e]) return !0;
        return !1
    }, n.prototype.leave = function(t) {
        var n = t instanceof this.constructor ? t : e(t.currentTarget).data("bs." + this.type);
        if (n || (n = new this.constructor(t.currentTarget, this.getDelegateOptions()), e(t.currentTarget).data("bs." + this.type, n)), t instanceof e.Event && (n.inState["focusout" == t.type ? "focus" : "hover"] = !1), !n.isInStateTrue()) return clearTimeout(n.timeout), n.hoverState = "out", n.options.delay && n.options.delay.hide ? void(n.timeout = setTimeout(function() {
            "out" == n.hoverState && n.hide()
        }, n.options.delay.hide)) : n.hide()
    }, n.prototype.show = function() {
        var t = e.Event("show.bs." + this.type);
        if (this.hasContent() && this.enabled) {
            this.$element.trigger(t);
            var i = e.contains(this.$element[0].ownerDocument.documentElement, this.$element[0]);
            if (t.isDefaultPrevented() || !i) return;
            var o = this,
                s = this.tip(),
                r = this.getUID(this.type);
            this.setContent(), s.attr("id", r), this.$element.attr("aria-describedby", r), this.options.animation && s.addClass("fade");
            var a = "function" == typeof this.options.placement ? this.options.placement.call(this, s[0], this.$element[0]) : this.options.placement,
                l = /\s?auto?\s?/i,
                c = l.test(a);
            c && (a = a.replace(l, "") || "top"), s.detach().css({
                top: 0,
                left: 0,
                display: "block"
            }).addClass(a).data("bs." + this.type, this), this.options.container ? s.appendTo(this.options.container) : s.insertAfter(this.$element), this.$element.trigger("inserted.bs." + this.type);
            var u = this.getPosition(),
                d = s[0].offsetWidth,
                h = s[0].offsetHeight;
            if (c) {
                var f = a,
                    p = this.getPosition(this.$viewport);
                a = "bottom" == a && u.bottom + h > p.bottom ? "top" : "top" == a && u.top - h < p.top ? "bottom" : "right" == a && u.right + d > p.width ? "left" : "left" == a && u.left - d < p.left ? "right" : a, s.removeClass(f).addClass(a)
            }
            var m = this.getCalculatedOffset(a, u, d, h);
            this.applyPlacement(m, a);
            var g = function() {
                var e = o.hoverState;
                o.$element.trigger("shown.bs." + o.type), o.hoverState = null, "out" == e && o.leave(o)
            };
            e.support.transition && this.$tip.hasClass("fade") ? s.one("bsTransitionEnd", g).emulateTransitionEnd(n.TRANSITION_DURATION) : g()
        }
    }, n.prototype.applyPlacement = function(t, n) {
        var i = this.tip(),
            o = i[0].offsetWidth,
            s = i[0].offsetHeight,
            r = parseInt(i.css("margin-top"), 10),
            a = parseInt(i.css("margin-left"), 10);
        isNaN(r) && (r = 0), isNaN(a) && (a = 0), t.top += r, t.left += a, e.offset.setOffset(i[0], e.extend({
            using: function(e) {
                i.css({
                    top: Math.round(e.top),
                    left: Math.round(e.left)
                })
            }
        }, t), 0), i.addClass("in");
        var l = i[0].offsetWidth,
            c = i[0].offsetHeight;
        "top" == n && c != s && (t.top = t.top + s - c);
        var u = this.getViewportAdjustedDelta(n, t, l, c);
        u.left ? t.left += u.left : t.top += u.top;
        var d = /top|bottom/.test(n),
            h = d ? 2 * u.left - o + l : 2 * u.top - s + c,
            f = d ? "offsetWidth" : "offsetHeight";
        i.offset(t), this.replaceArrow(h, i[0][f], d)
    }, n.prototype.replaceArrow = function(e, t, n) {
        this.arrow().css(n ? "left" : "top", 50 * (1 - e / t) + "%").css(n ? "top" : "left", "")
    }, n.prototype.setContent = function() {
        var e = this.tip(),
            t = this.getTitle();
        e.find(".tooltip-inner")[this.options.html ? "html" : "text"](t), e.removeClass("fade in top bottom left right")
    }, n.prototype.hide = function(t) {
        function i() {
            "in" != o.hoverState && s.detach(), o.$element && o.$element.removeAttr("aria-describedby").trigger("hidden.bs." + o.type), t && t()
        }
        var o = this,
            s = e(this.$tip),
            r = e.Event("hide.bs." + this.type);
        if (this.$element.trigger(r), !r.isDefaultPrevented()) return s.removeClass("in"), e.support.transition && s.hasClass("fade") ? s.one("bsTransitionEnd", i).emulateTransitionEnd(n.TRANSITION_DURATION) : i(), this.hoverState = null, this
    }, n.prototype.fixTitle = function() {
        var e = this.$element;
        (e.attr("title") || "string" != typeof e.attr("data-original-title")) && e.attr("data-original-title", e.attr("title") || "").attr("title", "")
    }, n.prototype.hasContent = function() {
        return this.getTitle()
    }, n.prototype.getPosition = function(t) {
        t = t || this.$element;
        var n = t[0],
            i = "BODY" == n.tagName,
            o = n.getBoundingClientRect();
        null == o.width && (o = e.extend({}, o, {
            width: o.right - o.left,
            height: o.bottom - o.top
        }));
        var s = window.SVGElement && n instanceof window.SVGElement,
            r = i ? {
                top: 0,
                left: 0
            } : s ? null : t.offset(),
            a = {
                scroll: i ? document.documentElement.scrollTop || document.body.scrollTop : t.scrollTop()
            },
            l = i ? {
                width: e(window).width(),
                height: e(window).height()
            } : null;
        return e.extend({}, o, a, l, r)
    }, n.prototype.getCalculatedOffset = function(e, t, n, i) {
        return "bottom" == e ? {
            top: t.top + t.height,
            left: t.left + t.width / 2 - n / 2
        } : "top" == e ? {
            top: t.top - i,
            left: t.left + t.width / 2 - n / 2
        } : "left" == e ? {
            top: t.top + t.height / 2 - i / 2,
            left: t.left - n
        } : {
            top: t.top + t.height / 2 - i / 2,
            left: t.left + t.width
        }
    }, n.prototype.getViewportAdjustedDelta = function(e, t, n, i) {
        var o = {
            top: 0,
            left: 0
        };
        if (!this.$viewport) return o;
        var s = this.options.viewport && this.options.viewport.padding || 0,
            r = this.getPosition(this.$viewport);
        if (/right|left/.test(e)) {
            var a = t.top - s - r.scroll,
                l = t.top + s - r.scroll + i;
            a < r.top ? o.top = r.top - a : l > r.top + r.height && (o.top = r.top + r.height - l)
        } else {
            var c = t.left - s,
                u = t.left + s + n;
            c < r.left ? o.left = r.left - c : u > r.right && (o.left = r.left + r.width - u)
        }
        return o
    }, n.prototype.getTitle = function() {
        var e, t = this.$element,
            n = this.options;
        return e = t.attr("data-original-title") || ("function" == typeof n.title ? n.title.call(t[0]) : n.title)
    }, n.prototype.getUID = function(e) {
        do e += ~~(1e6 * Math.random()); while (document.getElementById(e));
        return e
    }, n.prototype.tip = function() {
        if (!this.$tip && (this.$tip = e(this.options.template), 1 != this.$tip.length)) throw new Error(this.type + " `template` option must consist of exactly 1 top-level element!");
        return this.$tip
    }, n.prototype.arrow = function() {
        return this.$arrow = this.$arrow || this.tip().find(".tooltip-arrow")
    }, n.prototype.enable = function() {
        this.enabled = !0
    }, n.prototype.disable = function() {
        this.enabled = !1
    }, n.prototype.toggleEnabled = function() {
        this.enabled = !this.enabled
    }, n.prototype.toggle = function(t) {
        var n = this;
        t && (n = e(t.currentTarget).data("bs." + this.type), n || (n = new this.constructor(t.currentTarget, this.getDelegateOptions()), e(t.currentTarget).data("bs." + this.type, n))), t ? (n.inState.click = !n.inState.click, n.isInStateTrue() ? n.enter(n) : n.leave(n)) : n.tip().hasClass("in") ? n.leave(n) : n.enter(n)
    }, n.prototype.destroy = function() {
        var e = this;
        clearTimeout(this.timeout), this.hide(function() {
            e.$element.off("." + e.type).removeData("bs." + e.type), e.$tip && e.$tip.detach(), e.$tip = null, e.$arrow = null, e.$viewport = null, e.$element = null
        })
    };
    var i = e.fn.tooltip;
    e.fn.tooltip = t, e.fn.tooltip.Constructor = n, e.fn.tooltip.noConflict = function() {
        return e.fn.tooltip = i, this
    }
}(jQuery), + function(e) {
    "use strict";

    function t(t) {
        return this.each(function() {
            var i = e(this),
                o = i.data("bs.popover"),
                s = "object" == typeof t && t;
            !o && /destroy|hide/.test(t) || (o || i.data("bs.popover", o = new n(this, s)), "string" == typeof t && o[t]())
        })
    }
    var n = function(e, t) {
        this.init("popover", e, t)
    };
    if (!e.fn.tooltip) throw new Error("Popover requires tooltip.js");
    n.VERSION = "3.3.7", n.DEFAULTS = e.extend({}, e.fn.tooltip.Constructor.DEFAULTS, {
        placement: "right",
        trigger: "click",
        content: "",
        template: '<div class="popover" role="tooltip"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'
    }), n.prototype = e.extend({}, e.fn.tooltip.Constructor.prototype), n.prototype.constructor = n, n.prototype.getDefaults = function() {
        return n.DEFAULTS
    }, n.prototype.setContent = function() {
        var e = this.tip(),
            t = this.getTitle(),
            n = this.getContent();
        e.find(".popover-title")[this.options.html ? "html" : "text"](t), e.find(".popover-content").children().detach().end()[this.options.html ? "string" == typeof n ? "html" : "append" : "text"](n), e.removeClass("fade top bottom left right in"), e.find(".popover-title").html() || e.find(".popover-title").hide()
    }, n.prototype.hasContent = function() {
        return this.getTitle() || this.getContent()
    }, n.prototype.getContent = function() {
        var e = this.$element,
            t = this.options;
        return e.attr("data-content") || ("function" == typeof t.content ? t.content.call(e[0]) : t.content)
    }, n.prototype.arrow = function() {
        return this.$arrow = this.$arrow || this.tip().find(".arrow")
    };
    var i = e.fn.popover;
    e.fn.popover = t, e.fn.popover.Constructor = n, e.fn.popover.noConflict = function() {
        return e.fn.popover = i, this
    }
}(jQuery), + function(e) {
    "use strict";

    function t(n, i) {
        this.$body = e(document.body), this.$scrollElement = e(e(n).is(document.body) ? window : n), this.options = e.extend({}, t.DEFAULTS, i), this.selector = (this.options.target || "") + " .nav li > a", this.offsets = [], this.targets = [], this.activeTarget = null, this.scrollHeight = 0, this.$scrollElement.on("scroll.bs.scrollspy", e.proxy(this.process, this)), this.refresh(), this.process()
    }

    function n(n) {
        return this.each(function() {
            var i = e(this),
                o = i.data("bs.scrollspy"),
                s = "object" == typeof n && n;
            o || i.data("bs.scrollspy", o = new t(this, s)), "string" == typeof n && o[n]()
        })
    }
    t.VERSION = "3.3.7", t.DEFAULTS = {
        offset: 10
    }, t.prototype.getScrollHeight = function() {
        return this.$scrollElement[0].scrollHeight || Math.max(this.$body[0].scrollHeight, document.documentElement.scrollHeight)
    }, t.prototype.refresh = function() {
        var t = this,
            n = "offset",
            i = 0;
        this.offsets = [], this.targets = [], this.scrollHeight = this.getScrollHeight(), e.isWindow(this.$scrollElement[0]) || (n = "position", i = this.$scrollElement.scrollTop()), this.$body.find(this.selector).map(function() {
            var t = e(this),
                o = t.data("target") || t.attr("href"),
                s = /^#./.test(o) && e(o);
            return s && s.length && s.is(":visible") && [
                [s[n]().top + i, o]
            ] || null
        }).sort(function(e, t) {
            return e[0] - t[0]
        }).each(function() {
            t.offsets.push(this[0]), t.targets.push(this[1])
        })
    }, t.prototype.process = function() {
        var e, t = this.$scrollElement.scrollTop() + this.options.offset,
            n = this.getScrollHeight(),
            i = this.options.offset + n - this.$scrollElement.height(),
            o = this.offsets,
            s = this.targets,
            r = this.activeTarget;
        if (this.scrollHeight != n && this.refresh(), t >= i) return r != (e = s[s.length - 1]) && this.activate(e);
        if (r && t < o[0]) return this.activeTarget = null, this.clear();
        for (e = o.length; e--;) r != s[e] && t >= o[e] && (void 0 === o[e + 1] || t < o[e + 1]) && this.activate(s[e])
    }, t.prototype.activate = function(t) {
        this.activeTarget = t, this.clear();
        var n = this.selector + '[data-target="' + t + '"],' + this.selector + '[href="' + t + '"]',
            i = e(n).parents("li").addClass("active");
        i.parent(".dropdown-menu").length && (i = i.closest("li.dropdown").addClass("active")), i.trigger("activate.bs.scrollspy")
    }, t.prototype.clear = function() {
        e(this.selector).parentsUntil(this.options.target, ".active").removeClass("active")
    };
    var i = e.fn.scrollspy;
    e.fn.scrollspy = n, e.fn.scrollspy.Constructor = t, e.fn.scrollspy.noConflict = function() {
        return e.fn.scrollspy = i, this
    }, e(window).on("load.bs.scrollspy.data-api", function() {
        e('[data-spy="scroll"]').each(function() {
            var t = e(this);
            n.call(t, t.data())
        })
    })
}(jQuery), + function(e) {
    "use strict";

    function t(t) {
        return this.each(function() {
            var i = e(this),
                o = i.data("bs.tab");
            o || i.data("bs.tab", o = new n(this)), "string" == typeof t && o[t]()
        })
    }
    var n = function(t) {
        this.element = e(t)
    };
    n.VERSION = "3.3.7", n.TRANSITION_DURATION = 150, n.prototype.show = function() {
        var t = this.element,
            n = t.closest("ul:not(.dropdown-menu)"),
            i = t.data("target");
        if (i || (i = t.attr("href"), i = i && i.replace(/.*(?=#[^\s]*$)/, "")), !t.parent("li").hasClass("active")) {
            var o = n.find(".active:last a"),
                s = e.Event("hide.bs.tab", {
                    relatedTarget: t[0]
                }),
                r = e.Event("show.bs.tab", {
                    relatedTarget: o[0]
                });
            if (o.trigger(s), t.trigger(r), !r.isDefaultPrevented() && !s.isDefaultPrevented()) {
                var a = e(i);
                this.activate(t.closest("li"), n), this.activate(a, a.parent(), function() {
                    o.trigger({
                        type: "hidden.bs.tab",
                        relatedTarget: t[0]
                    }), t.trigger({
                        type: "shown.bs.tab",
                        relatedTarget: o[0]
                    })
                })
            }
        }
    }, n.prototype.activate = function(t, i, o) {
        function s() {
            r.removeClass("active").find("> .dropdown-menu > .active").removeClass("active").end().find('[data-toggle="tab"]').attr("aria-expanded", !1), t.addClass("active").find('[data-toggle="tab"]').attr("aria-expanded", !0), a ? (t[0].offsetWidth, t.addClass("in")) : t.removeClass("fade"), t.parent(".dropdown-menu").length && t.closest("li.dropdown").addClass("active").end().find('[data-toggle="tab"]').attr("aria-expanded", !0), o && o()
        }
        var r = i.find("> .active"),
            a = o && e.support.transition && (r.length && r.hasClass("fade") || !!i.find("> .fade").length);
        r.length && a ? r.one("bsTransitionEnd", s).emulateTransitionEnd(n.TRANSITION_DURATION) : s(), r.removeClass("in")
    };
    var i = e.fn.tab;
    e.fn.tab = t, e.fn.tab.Constructor = n, e.fn.tab.noConflict = function() {
        return e.fn.tab = i, this
    };
    var o = function(n) {
        n.preventDefault(), t.call(e(this), "show")
    };
    e(document).on("click.bs.tab.data-api", '[data-toggle="tab"]', o).on("click.bs.tab.data-api", '[data-toggle="pill"]', o)
}(jQuery), + function(e) {
    "use strict";

    function t(t) {
        return this.each(function() {
            var i = e(this),
                o = i.data("bs.affix"),
                s = "object" == typeof t && t;
            o || i.data("bs.affix", o = new n(this, s)), "string" == typeof t && o[t]()
        })
    }
    var n = function(t, i) {
        this.options = e.extend({}, n.DEFAULTS, i), this.$target = e(this.options.target).on("scroll.bs.affix.data-api", e.proxy(this.checkPosition, this)).on("click.bs.affix.data-api", e.proxy(this.checkPositionWithEventLoop, this)), this.$element = e(t), this.affixed = null, this.unpin = null, this.pinnedOffset = null, this.checkPosition()
    };
    n.VERSION = "3.3.7", n.RESET = "affix affix-top affix-bottom", n.DEFAULTS = {
        offset: 0,
        target: window
    }, n.prototype.getState = function(e, t, n, i) {
        var o = this.$target.scrollTop(),
            s = this.$element.offset(),
            r = this.$target.height();
        if (null != n && "top" == this.affixed) return o < n && "top";
        if ("bottom" == this.affixed) return null != n ? !(o + this.unpin <= s.top) && "bottom" : !(o + r <= e - i) && "bottom";
        var a = null == this.affixed,
            l = a ? o : s.top,
            c = a ? r : t;
        return null != n && o <= n ? "top" : null != i && l + c >= e - i && "bottom"
    }, n.prototype.getPinnedOffset = function() {
        if (this.pinnedOffset) return this.pinnedOffset;
        this.$element.removeClass(n.RESET).addClass("affix");
        var e = this.$target.scrollTop(),
            t = this.$element.offset();
        return this.pinnedOffset = t.top - e
    }, n.prototype.checkPositionWithEventLoop = function() {
        setTimeout(e.proxy(this.checkPosition, this), 1)
    }, n.prototype.checkPosition = function() {
        if (this.$element.is(":visible")) {
            var t = this.$element.height(),
                i = this.options.offset,
                o = i.top,
                s = i.bottom,
                r = Math.max(e(document).height(), e(document.body).height());
            "object" != typeof i && (s = o = i), "function" == typeof o && (o = i.top(this.$element)), "function" == typeof s && (s = i.bottom(this.$element));
            var a = this.getState(r, t, o, s);
            if (this.affixed != a) {
                null != this.unpin && this.$element.css("top", "");
                var l = "affix" + (a ? "-" + a : ""),
                    c = e.Event(l + ".bs.affix");
                if (this.$element.trigger(c), c.isDefaultPrevented()) return;
                this.affixed = a, this.unpin = "bottom" == a ? this.getPinnedOffset() : null, this.$element.removeClass(n.RESET).addClass(l).trigger(l.replace("affix", "affixed") + ".bs.affix")
            }
            "bottom" == a && this.$element.offset({
                top: r - t - s
            })
        }
    };
    var i = e.fn.affix;
    e.fn.affix = t, e.fn.affix.Constructor = n, e.fn.affix.noConflict = function() {
        return e.fn.affix = i, this
    }, e(window).on("load", function() {
        e('[data-spy="affix"]').each(function() {
            var n = e(this),
                i = n.data();
            i.offset = i.offset || {}, null != i.offsetBottom && (i.offset.bottom = i.offsetBottom), null != i.offsetTop && (i.offset.top = i.offsetTop), t.call(n, i)
        })
    })
}(jQuery),
function(e) {
    "function" == typeof define && define.amd ? define(["jquery"], e) : e("object" == typeof exports ? require("jquery") : jQuery)
}(function(e, t) {
    function n() {
        var e, t, n, i, o, s, r, a;
        if (t = (new Date).toString(), n = (null != (r = t.split("(")[1]) ? r.slice(0, -1) : 0) || t.split(" "), n instanceof Array) {
            s = [];
            for (var i = 0, o = n.length; i < o; i++) a = n[i], ((e = null !== (r = a.match(/\b[A-Z]+\b/))) ? r[0] : 0) && s.push(e);
            n = s.pop()
        }
        return n
    }

    function i() {
        return new Date(Date.UTC.apply(Date, arguments))
    }
    "indexOf" in Array.prototype || (Array.prototype.indexOf = function(e, n) {
        n === t && (n = 0), n < 0 && (n += this.length), n < 0 && (n = 0);
        for (var i = this.length; n < i; n++)
            if (n in this && this[n] === e) return n;
        return -1
    });
    var o = function(i, o) {
        var s = this;
        this.element = e(i), this.container = o.container || "body", this.language = o.language || this.element.data("date-language") || "en", this.language = this.language in r ? this.language : this.language.split("-")[0], this.language = this.language in r ? this.language : "en", this.isRTL = r[this.language].rtl || !1, this.formatType = o.formatType || this.element.data("format-type") || "standard", this.format = a.parseFormat(o.format || this.element.data("date-format") || r[this.language].format || a.getDefaultFormat(this.formatType, "input"), this.formatType), this.isInline = !1, this.isVisible = !1, this.isInput = this.element.is("input"), this.fontAwesome = o.fontAwesome || this.element.data("font-awesome") || !1, this.bootcssVer = o.bootcssVer || (this.isInput ? this.element.is(".form-control") ? 3 : 2 : this.bootcssVer = this.element.is(".input-group") ? 3 : 2), this.component = !!this.element.is(".date") && (3 === this.bootcssVer ? this.element.find(".input-group-addon .glyphicon-th, .input-group-addon .glyphicon-time, .input-group-addon .glyphicon-remove, .input-group-addon .glyphicon-calendar, .input-group-addon .fa-calendar, .input-group-addon .fa-clock-o").parent() : this.element.find(".add-on .icon-th, .add-on .icon-time, .add-on .icon-calendar, .add-on .fa-calendar, .add-on .fa-clock-o").parent()), this.componentReset = !!this.element.is(".date") && (3 === this.bootcssVer ? this.element.find(".input-group-addon .glyphicon-remove, .input-group-addon .fa-times").parent() : this.element.find(".add-on .icon-remove, .add-on .fa-times").parent()), this.hasInput = this.component && this.element.find("input").length, this.component && 0 === this.component.length && (this.component = !1), this.linkField = o.linkField || this.element.data("link-field") || !1, this.linkFormat = a.parseFormat(o.linkFormat || this.element.data("link-format") || a.getDefaultFormat(this.formatType, "link"), this.formatType), this.minuteStep = o.minuteStep || this.element.data("minute-step") || 5, this.pickerPosition = o.pickerPosition || this.element.data("picker-position") || "bottom-right", this.showMeridian = o.showMeridian || this.element.data("show-meridian") || !1, this.initialDate = o.initialDate || new Date, this.zIndex = o.zIndex || this.element.data("z-index") || t, this.title = "undefined" != typeof o.title && o.title, this.timezone = o.timezone || n(), this.icons = {
            leftArrow: this.fontAwesome ? "fa-arrow-left" : 3 === this.bootcssVer ? "glyphicon-arrow-left" : "icon-arrow-left",
            rightArrow: this.fontAwesome ? "fa-arrow-right" : 3 === this.bootcssVer ? "glyphicon-arrow-right" : "icon-arrow-right"
        }, this.icontype = this.fontAwesome ? "fa" : "glyphicon", this._attachEvents(), this.clickedOutside = function(t) {
            0 === e(t.target).closest(".datetimepicker").length && s.hide()
        }, this.formatViewType = "datetime", "formatViewType" in o ? this.formatViewType = o.formatViewType : "formatViewType" in this.element.data() && (this.formatViewType = this.element.data("formatViewType")), this.minView = 0, "minView" in o ? this.minView = o.minView : "minView" in this.element.data() && (this.minView = this.element.data("min-view")), this.minView = a.convertViewMode(this.minView), this.maxView = a.modes.length - 1, "maxView" in o ? this.maxView = o.maxView : "maxView" in this.element.data() && (this.maxView = this.element.data("max-view")), this.maxView = a.convertViewMode(this.maxView), this.wheelViewModeNavigation = !1, "wheelViewModeNavigation" in o ? this.wheelViewModeNavigation = o.wheelViewModeNavigation : "wheelViewModeNavigation" in this.element.data() && (this.wheelViewModeNavigation = this.element.data("view-mode-wheel-navigation")), this.wheelViewModeNavigationInverseDirection = !1, "wheelViewModeNavigationInverseDirection" in o ? this.wheelViewModeNavigationInverseDirection = o.wheelViewModeNavigationInverseDirection : "wheelViewModeNavigationInverseDirection" in this.element.data() && (this.wheelViewModeNavigationInverseDirection = this.element.data("view-mode-wheel-navigation-inverse-dir")), this.wheelViewModeNavigationDelay = 100, "wheelViewModeNavigationDelay" in o ? this.wheelViewModeNavigationDelay = o.wheelViewModeNavigationDelay : "wheelViewModeNavigationDelay" in this.element.data() && (this.wheelViewModeNavigationDelay = this.element.data("view-mode-wheel-navigation-delay")), this.startViewMode = 2, "startView" in o ? this.startViewMode = o.startView : "startView" in this.element.data() && (this.startViewMode = this.element.data("start-view")), this.startViewMode = a.convertViewMode(this.startViewMode), this.viewMode = this.startViewMode, this.viewSelect = this.minView, "viewSelect" in o ? this.viewSelect = o.viewSelect : "viewSelect" in this.element.data() && (this.viewSelect = this.element.data("view-select")), this.viewSelect = a.convertViewMode(this.viewSelect), this.forceParse = !0, "forceParse" in o ? this.forceParse = o.forceParse : "dateForceParse" in this.element.data() && (this.forceParse = this.element.data("date-force-parse"));
        for (var l = 3 === this.bootcssVer ? a.templateV3 : a.template; l.indexOf("{iconType}") !== -1;) l = l.replace("{iconType}", this.icontype);
        for (; l.indexOf("{leftArrow}") !== -1;) l = l.replace("{leftArrow}", this.icons.leftArrow);
        for (; l.indexOf("{rightArrow}") !== -1;) l = l.replace("{rightArrow}", this.icons.rightArrow);
        if (this.picker = e(l).appendTo(this.isInline ? this.element : this.container).on({
            click: e.proxy(this.click, this),
            mousedown: e.proxy(this.mousedown, this)
        }), this.wheelViewModeNavigation && (e.fn.mousewheel ? this.picker.on({
            mousewheel: e.proxy(this.mousewheel, this)
        }) : console.log("Mouse Wheel event is not supported. Please include the jQuery Mouse Wheel plugin before enabling this option")), this.isInline ? this.picker.addClass("datetimepicker-inline") : this.picker.addClass("datetimepicker-dropdown-" + this.pickerPosition + " dropdown-menu"), this.isRTL) {
            this.picker.addClass("datetimepicker-rtl");
            var c = 3 === this.bootcssVer ? ".prev span, .next span" : ".prev i, .next i";
            this.picker.find(c).toggleClass(this.icons.leftArrow + " " + this.icons.rightArrow)
        }
        e(document).on("mousedown touchend", this.clickedOutside), this.autoclose = !1, "autoclose" in o ? this.autoclose = o.autoclose : "dateAutoclose" in this.element.data() && (this.autoclose = this.element.data("date-autoclose")), this.keyboardNavigation = !0, "keyboardNavigation" in o ? this.keyboardNavigation = o.keyboardNavigation : "dateKeyboardNavigation" in this.element.data() && (this.keyboardNavigation = this.element.data("date-keyboard-navigation")), this.todayBtn = o.todayBtn || this.element.data("date-today-btn") || !1, this.clearBtn = o.clearBtn || this.element.data("date-clear-btn") || !1, this.todayHighlight = o.todayHighlight || this.element.data("date-today-highlight") || !1, this.weekStart = 0, "undefined" != typeof o.weekStart ? this.weekStart = o.weekStart : "undefined" != typeof this.element.data("date-weekstart") ? this.weekStart = this.element.data("date-weekstart") : "undefined" != typeof r[this.language].weekStart && (this.weekStart = r[this.language].weekStart), this.weekStart = this.weekStart % 7, this.weekEnd = (this.weekStart + 6) % 7, this.onRenderDay = function(e) {
            var t = (o.onRenderDay || function() {
                return []
            })(e);
            "string" == typeof t && (t = [t]);
            var n = ["day"];
            return n.concat(t ? t : [])
        }, this.onRenderHour = function(e) {
            var t = (o.onRenderHour || function() {
                    return []
                })(e),
                n = ["hour"];
            return "string" == typeof t && (t = [t]), n.concat(t ? t : [])
        }, this.onRenderMinute = function(e) {
            var t = (o.onRenderMinute || function() {
                    return []
                })(e),
                n = ["minute"];
            return "string" == typeof t && (t = [t]), e < this.startDate || e > this.endDate ? n.push("disabled") : Math.floor(this.date.getUTCMinutes() / this.minuteStep) === Math.floor(e.getUTCMinutes() / this.minuteStep) && n.push("active"), n.concat(t ? t : [])
        }, this.onRenderYear = function(e) {
            var t = (o.onRenderYear || function() {
                    return []
                })(e),
                n = ["year"];
            "string" == typeof t && (t = [t]), this.date.getUTCFullYear() === e.getUTCFullYear() && n.push("active");
            var i = e.getUTCFullYear(),
                s = this.endDate.getUTCFullYear();
            return (e < this.startDate || i > s) && n.push("disabled"), n.concat(t ? t : [])
        }, this.onRenderMonth = function(e) {
            var t = (o.onRenderMonth || function() {
                    return []
                })(e),
                n = ["month"];
            return "string" == typeof t && (t = [t]), n.concat(t ? t : [])
        }, this.startDate = new Date((-8639968443048e3)), this.endDate = new Date(8639968443048e3), this.datesDisabled = [], this.daysOfWeekDisabled = [], this.setStartDate(o.startDate || this.element.data("date-startdate")), this.setEndDate(o.endDate || this.element.data("date-enddate")), this.setDatesDisabled(o.datesDisabled || this.element.data("date-dates-disabled")), this.setDaysOfWeekDisabled(o.daysOfWeekDisabled || this.element.data("date-days-of-week-disabled")), this.setMinutesDisabled(o.minutesDisabled || this.element.data("date-minute-disabled")), this.setHoursDisabled(o.hoursDisabled || this.element.data("date-hour-disabled")), this.fillDow(), this.fillMonths(), this.update(), this.showMode(), this.isInline && this.show()
    };
    o.prototype = {
        constructor: o,
        _events: [],
        _attachEvents: function() {
            this._detachEvents(), this.isInput ? this._events = [
                [this.element, {
                    focus: e.proxy(this.show, this),
                    keyup: e.proxy(this.update, this),
                    keydown: e.proxy(this.keydown, this)
                }]
            ] : this.component && this.hasInput ? (this._events = [
                [this.element.find("input"), {
                    focus: e.proxy(this.show, this),
                    keyup: e.proxy(this.update, this),
                    keydown: e.proxy(this.keydown, this)
                }],
                [this.component, {
                    click: e.proxy(this.show, this)
                }]
            ], this.componentReset && this._events.push([this.componentReset, {
                click: e.proxy(this.reset, this)
            }])) : this.element.is("div") ? this.isInline = !0 : this._events = [
                [this.element, {
                    click: e.proxy(this.show, this)
                }]
            ];
            for (var t, n, i = 0; i < this._events.length; i++) t = this._events[i][0], n = this._events[i][1], t.on(n)
        },
        _detachEvents: function() {
            for (var e, t, n = 0; n < this._events.length; n++) e = this._events[n][0], t = this._events[n][1], e.off(t);
            this._events = []
        },
        show: function(t) {
            this.picker.show(), this.height = this.component ? this.component.outerHeight() : this.element.outerHeight(), this.forceParse && this.update(), this.place(), e(window).on("resize", e.proxy(this.place, this)), t && (t.stopPropagation(), t.preventDefault()), this.isVisible = !0, this.element.trigger({
                type: "show",
                date: this.date
            })
        },
        hide: function() {
            this.isVisible && (this.isInline || (this.picker.hide(), e(window).off("resize", this.place), this.viewMode = this.startViewMode, this.showMode(), this.isInput || e(document).off("mousedown", this.hide), this.forceParse && (this.isInput && this.element.val() || this.hasInput && this.element.find("input").val()) && this.setValue(), this.isVisible = !1, this.element.trigger({
                type: "hide",
                date: this.date
            })))
        },
        remove: function() {
            this._detachEvents(), e(document).off("mousedown", this.clickedOutside), this.picker.remove(), delete this.picker, delete this.element.data().datetimepicker
        },
        getDate: function() {
            var e = this.getUTCDate();
            return null === e ? null : new Date(e.getTime() + 6e4 * e.getTimezoneOffset())
        },
        getUTCDate: function() {
            return this.date
        },
        getInitialDate: function() {
            return this.initialDate
        },
        setInitialDate: function(e) {
            this.initialDate = e
        },
        setDate: function(e) {
            this.setUTCDate(new Date(e.getTime() - 6e4 * e.getTimezoneOffset()))
        },
        setUTCDate: function(e) {
            e >= this.startDate && e <= this.endDate ? (this.date = e, this.setValue(), this.viewDate = this.date, this.fill()) : this.element.trigger({
                type: "outOfRange",
                date: e,
                startDate: this.startDate,
                endDate: this.endDate
            })
        },
        setFormat: function(e) {
            this.format = a.parseFormat(e, this.formatType);
            var t;
            this.isInput ? t = this.element : this.component && (t = this.element.find("input")), t && t.val() && this.setValue()
        },
        setValue: function() {
            var t = this.getFormattedDate();
            this.isInput ? this.element.val(t) : (this.component && this.element.find("input").val(t), this.element.data("date", t)), this.linkField && e("#" + this.linkField).val(this.getFormattedDate(this.linkFormat))
        },
        getFormattedDate: function(e) {
            return e = e || this.format, a.formatDate(this.date, e, this.language, this.formatType, this.timezone)
        },
        setStartDate: function(e) {
            this.startDate = e || this.startDate, 8639968443048e3 !== this.startDate.valueOf() && (this.startDate = a.parseDate(this.startDate, this.format, this.language, this.formatType, this.timezone)), this.update(), this.updateNavArrows()
        },
        setEndDate: function(e) {
            this.endDate = e || this.endDate, 8639968443048e3 !== this.endDate.valueOf() && (this.endDate = a.parseDate(this.endDate, this.format, this.language, this.formatType, this.timezone)), this.update(), this.updateNavArrows()
        },
        setDatesDisabled: function(t) {
            this.datesDisabled = t || [], e.isArray(this.datesDisabled) || (this.datesDisabled = this.datesDisabled.split(/,\s*/));
            var n = this;
            this.datesDisabled = e.map(this.datesDisabled, function(e) {
                return a.parseDate(e, n.format, n.language, n.formatType, n.timezone).toDateString()
            }), this.update(), this.updateNavArrows()
        },
        setTitle: function(e, t) {
            return this.picker.find(e).find("th:eq(1)").text(this.title === !1 ? t : this.title)
        },
        setDaysOfWeekDisabled: function(t) {
            this.daysOfWeekDisabled = t || [], e.isArray(this.daysOfWeekDisabled) || (this.daysOfWeekDisabled = this.daysOfWeekDisabled.split(/,\s*/)), this.daysOfWeekDisabled = e.map(this.daysOfWeekDisabled, function(e) {
                return parseInt(e, 10)
            }), this.update(), this.updateNavArrows()
        },
        setMinutesDisabled: function(t) {
            this.minutesDisabled = t || [], e.isArray(this.minutesDisabled) || (this.minutesDisabled = this.minutesDisabled.split(/,\s*/)), this.minutesDisabled = e.map(this.minutesDisabled, function(e) {
                return parseInt(e, 10)
            }), this.update(), this.updateNavArrows()
        },
        setHoursDisabled: function(t) {
            this.hoursDisabled = t || [], e.isArray(this.hoursDisabled) || (this.hoursDisabled = this.hoursDisabled.split(/,\s*/)), this.hoursDisabled = e.map(this.hoursDisabled, function(e) {
                return parseInt(e, 10)
            }), this.update(), this.updateNavArrows()
        },
        place: function() {
            if (!this.isInline) {
                if (!this.zIndex) {
                    var t = 0;
                    e("div").each(function() {
                        var n = parseInt(e(this).css("zIndex"), 10);
                        n > t && (t = n)
                    }), this.zIndex = t + 10
                }
                var n, i, o, s;
                s = this.container instanceof e ? this.container.offset() : e(this.container).offset(), this.component ? (n = this.component.offset(), o = n.left, "bottom-left" !== this.pickerPosition && "top-left" !== this.pickerPosition || (o += this.component.outerWidth() - this.picker.outerWidth())) : (n = this.element.offset(), o = n.left, "bottom-left" !== this.pickerPosition && "top-left" !== this.pickerPosition || (o += this.element.outerWidth() - this.picker.outerWidth()));
                var r = document.body.clientWidth || window.innerWidth;
                o + 220 > r && (o = r - 220), i = "top-left" === this.pickerPosition || "top-right" === this.pickerPosition ? n.top - this.picker.outerHeight() : n.top + this.height, i -= s.top, o -= s.left, this.picker.css({
                    top: i,
                    left: o,
                    zIndex: this.zIndex
                })
            }
        },
        hour_minute: "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]",
        update: function() {
            var e, t = !1;
            arguments && arguments.length && ("string" == typeof arguments[0] || arguments[0] instanceof Date) ? (e = arguments[0], t = !0) : (e = (this.isInput ? this.element.val() : this.element.find("input").val()) || this.element.data("date") || this.initialDate, "string" == typeof e && (e = e.replace(/^\s+|\s+$/g, ""))), e || (e = new Date, t = !1), "string" == typeof e && (new RegExp(this.hour_minute).test(e) || new RegExp(this.hour_minute + ":[0-5][0-9]").test(e)) && (e = this.getDate()), this.date = a.parseDate(e, this.format, this.language, this.formatType, this.timezone), t && this.setValue(), this.date < this.startDate ? this.viewDate = new Date(this.startDate) : this.date > this.endDate ? this.viewDate = new Date(this.endDate) : this.viewDate = new Date(this.date), this.fill()
        },
        fillDow: function() {
            for (var e = this.weekStart, t = "<tr>"; e < this.weekStart + 7;) t += '<th class="dow">' + r[this.language].daysMin[e++ % 7] + "</th>";
            t += "</tr>", this.picker.find(".datetimepicker-days thead").append(t)
        },
        fillMonths: function() {
            for (var e = "", t = new Date(this.viewDate), n = 0; n < 12; n++) {
                t.setUTCMonth(n);
                var i = this.onRenderMonth(t);
                e += '<span class="' + i.join(" ") + '">' + r[this.language].monthsShort[n] + "</span>"
            }
            this.picker.find(".datetimepicker-months td").html(e)
        },
        fill: function() {
            if (this.date && this.viewDate) {
                var t = new Date(this.viewDate),
                    n = t.getUTCFullYear(),
                    o = t.getUTCMonth(),
                    l = t.getUTCDate(),
                    c = t.getUTCHours(),
                    u = this.startDate.getUTCFullYear(),
                    d = this.startDate.getUTCMonth(),
                    h = this.endDate.getUTCFullYear(),
                    f = this.endDate.getUTCMonth() + 1,
                    p = new i(this.date.getUTCFullYear(), this.date.getUTCMonth(), this.date.getUTCDate()).valueOf(),
                    m = new Date;
                if (this.setTitle(".datetimepicker-days", r[this.language].months[o] + " " + n), "time" === this.formatViewType) {
                    var g = this.getFormattedDate();
                    this.setTitle(".datetimepicker-hours", g), this.setTitle(".datetimepicker-minutes", g)
                } else this.setTitle(".datetimepicker-hours", l + " " + r[this.language].months[o] + " " + n), this.setTitle(".datetimepicker-minutes", l + " " + r[this.language].months[o] + " " + n);
                this.picker.find("tfoot th.today").text(r[this.language].today || r.en.today).toggle(this.todayBtn !== !1), this.picker.find("tfoot th.clear").text(r[this.language].clear || r.en.clear).toggle(this.clearBtn !== !1), this.updateNavArrows(), this.fillMonths();
                var v = i(n, o - 1, 28, 0, 0, 0, 0),
                    y = a.getDaysInMonth(v.getUTCFullYear(), v.getUTCMonth());
                v.setUTCDate(y), v.setUTCDate(y - (v.getUTCDay() - this.weekStart + 7) % 7);
                var b = new Date(v);
                b.setUTCDate(b.getUTCDate() + 42), b = b.valueOf();
                for (var w, T = []; v.valueOf() < b;) v.getUTCDay() === this.weekStart && T.push("<tr>"), w = this.onRenderDay(v), v.getUTCFullYear() < n || v.getUTCFullYear() === n && v.getUTCMonth() < o ? w.push("old") : (v.getUTCFullYear() > n || v.getUTCFullYear() === n && v.getUTCMonth() > o) && w.push("new"), this.todayHighlight && v.getUTCFullYear() === m.getFullYear() && v.getUTCMonth() === m.getMonth() && v.getUTCDate() === m.getDate() && w.push("today"), v.valueOf() === p && w.push("active"), (v.valueOf() + 864e5 <= this.startDate || v.valueOf() > this.endDate || e.inArray(v.getUTCDay(), this.daysOfWeekDisabled) !== -1 || e.inArray(v.toDateString(), this.datesDisabled) !== -1) && w.push("disabled"), T.push('<td class="' + w.join(" ") + '">' + v.getUTCDate() + "</td>"), v.getUTCDay() === this.weekEnd && T.push("</tr>"), v.setUTCDate(v.getUTCDate() + 1);
                this.picker.find(".datetimepicker-days tbody").empty().append(T.join("")), T = [];
                var x = "",
                    C = "",
                    D = "",
                    k = this.hoursDisabled || [];
                t = new Date(this.viewDate);
                for (var S = 0; S < 24; S++) {
                    t.setUTCHours(S), w = this.onRenderHour(t), k.indexOf(S) !== -1 && w.push("disabled");
                    var E = i(n, o, l, S);
                    E.valueOf() + 36e5 <= this.startDate || E.valueOf() > this.endDate ? w.push("disabled") : c === S && w.push("active"), this.showMeridian && 2 === r[this.language].meridiem.length ? (C = S < 12 ? r[this.language].meridiem[0] : r[this.language].meridiem[1], C !== D && ("" !== D && T.push("</fieldset>"), T.push('<fieldset class="hour"><legend>' + C.toUpperCase() + "</legend>")), D = C, x = S % 12 ? S % 12 : 12, S < 12 ? w.push("hour_am") : w.push("hour_pm"), T.push('<span class="' + w.join(" ") + '">' + x + "</span>"), 23 === S && T.push("</fieldset>")) : (x = S + ":00", T.push('<span class="' + w.join(" ") + '">' + x + "</span>"))
                }
                this.picker.find(".datetimepicker-hours td").html(T.join("")), T = [], x = "", C = "", D = "";
                var M = this.minutesDisabled || [];
                t = new Date(this.viewDate);
                for (var S = 0; S < 60; S += this.minuteStep) M.indexOf(S) === -1 && (t.setUTCMinutes(S), t.setUTCSeconds(0), w = this.onRenderMinute(t), this.showMeridian && 2 === r[this.language].meridiem.length ? (C = c < 12 ? r[this.language].meridiem[0] : r[this.language].meridiem[1], C !== D && ("" !== D && T.push("</fieldset>"), T.push('<fieldset class="minute"><legend>' + C.toUpperCase() + "</legend>")), D = C, x = c % 12 ? c % 12 : 12, T.push('<span class="' + w.join(" ") + '">' + x + ":" + (S < 10 ? "0" + S : S) + "</span>"), 59 === S && T.push("</fieldset>")) : (x = S + ":00", T.push('<span class="' + w.join(" ") + '">' + c + ":" + (S < 10 ? "0" + S : S) + "</span>")));
                this.picker.find(".datetimepicker-minutes td").html(T.join(""));
                var N = this.date.getUTCFullYear(),
                    A = this.setTitle(".datetimepicker-months", n).end().find(".month").removeClass("active");
                N === n && A.eq(this.date.getUTCMonth()).addClass("active"), (n < u || n > h) && A.addClass("disabled"), n === u && A.slice(0, d).addClass("disabled"), n === h && A.slice(f).addClass("disabled"), T = "", n = 10 * parseInt(n / 10, 10);
                var I = this.setTitle(".datetimepicker-years", n + "-" + (n + 9)).end().find("td");
                n -= 1, t = new Date(this.viewDate);
                for (var S = -1; S < 11; S++) t.setUTCFullYear(n), w = this.onRenderYear(t), S !== -1 && 10 !== S || w.push(s), T += '<span class="' + w.join(" ") + '">' + n + "</span>", n += 1;
                I.html(T), this.place()
            }
        },
        updateNavArrows: function() {
            var e = new Date(this.viewDate),
                t = e.getUTCFullYear(),
                n = e.getUTCMonth(),
                i = e.getUTCDate(),
                o = e.getUTCHours();
            switch (this.viewMode) {
                case 0:
                    t <= this.startDate.getUTCFullYear() && n <= this.startDate.getUTCMonth() && i <= this.startDate.getUTCDate() && o <= this.startDate.getUTCHours() ? this.picker.find(".prev").css({
                        visibility: "hidden"
                    }) : this.picker.find(".prev").css({
                        visibility: "visible"
                    }), t >= this.endDate.getUTCFullYear() && n >= this.endDate.getUTCMonth() && i >= this.endDate.getUTCDate() && o >= this.endDate.getUTCHours() ? this.picker.find(".next").css({
                        visibility: "hidden"
                    }) : this.picker.find(".next").css({
                        visibility: "visible"
                    });
                    break;
                case 1:
                    t <= this.startDate.getUTCFullYear() && n <= this.startDate.getUTCMonth() && i <= this.startDate.getUTCDate() ? this.picker.find(".prev").css({
                        visibility: "hidden"
                    }) : this.picker.find(".prev").css({
                        visibility: "visible"
                    }), t >= this.endDate.getUTCFullYear() && n >= this.endDate.getUTCMonth() && i >= this.endDate.getUTCDate() ? this.picker.find(".next").css({
                        visibility: "hidden"
                    }) : this.picker.find(".next").css({
                        visibility: "visible"
                    });
                    break;
                case 2:
                    t <= this.startDate.getUTCFullYear() && n <= this.startDate.getUTCMonth() ? this.picker.find(".prev").css({
                        visibility: "hidden"
                    }) : this.picker.find(".prev").css({
                        visibility: "visible"
                    }), t >= this.endDate.getUTCFullYear() && n >= this.endDate.getUTCMonth() ? this.picker.find(".next").css({
                        visibility: "hidden"
                    }) : this.picker.find(".next").css({
                        visibility: "visible"
                    });
                    break;
                case 3:
                case 4:
                    t <= this.startDate.getUTCFullYear() ? this.picker.find(".prev").css({
                        visibility: "hidden"
                    }) : this.picker.find(".prev").css({
                        visibility: "visible"
                    }), t >= this.endDate.getUTCFullYear() ? this.picker.find(".next").css({
                        visibility: "hidden"
                    }) : this.picker.find(".next").css({
                        visibility: "visible"
                    })
            }
        },
        mousewheel: function(t) {
            if (t.preventDefault(), t.stopPropagation(), !this.wheelPause) {
                this.wheelPause = !0;
                var n = t.originalEvent,
                    i = n.wheelDelta,
                    o = i > 0 ? 1 : 0 === i ? 0 : -1;
                this.wheelViewModeNavigationInverseDirection && (o = -o), this.showMode(o), setTimeout(e.proxy(function() {
                    this.wheelPause = !1
                }, this), this.wheelViewModeNavigationDelay)
            }
        },
        click: function(t) {
            t.stopPropagation(), t.preventDefault();
            var n = e(t.target).closest("span, td, th, legend");
            if (n.is("." + this.icontype) && (n = e(n).parent().closest("span, td, th, legend")), 1 === n.length) {
                if (n.is(".disabled")) return void this.element.trigger({
                    type: "outOfRange",
                    date: this.viewDate,
                    startDate: this.startDate,
                    endDate: this.endDate
                });
                switch (n[0].nodeName.toLowerCase()) {
                    case "th":
                        switch (n[0].className) {
                            case "switch":
                                this.showMode(1);
                                break;
                            case "prev":
                            case "next":
                                var o = a.modes[this.viewMode].navStep * ("prev" === n[0].className ? -1 : 1);
                                switch (this.viewMode) {
                                    case 0:
                                        this.viewDate = this.moveHour(this.viewDate, o);
                                        break;
                                    case 1:
                                        this.viewDate = this.moveDate(this.viewDate, o);
                                        break;
                                    case 2:
                                        this.viewDate = this.moveMonth(this.viewDate, o);
                                        break;
                                    case 3:
                                    case 4:
                                        this.viewDate = this.moveYear(this.viewDate, o)
                                }
                                this.fill(), this.element.trigger({
                                    type: n[0].className + ":" + this.convertViewModeText(this.viewMode),
                                    date: this.viewDate,
                                    startDate: this.startDate,
                                    endDate: this.endDate
                                });
                                break;
                            case "clear":
                                this.reset(), this.autoclose && this.hide();
                                break;
                            case "today":
                                var s = new Date;
                                s = i(s.getFullYear(), s.getMonth(), s.getDate(), s.getHours(), s.getMinutes(), s.getSeconds(), 0), s < this.startDate ? s = this.startDate : s > this.endDate && (s = this.endDate), this.viewMode = this.startViewMode, this.showMode(0), this._setDate(s), this.fill(), this.autoclose && this.hide()
                        }
                        break;
                    case "span":
                        if (!n.is(".disabled")) {
                            var r = this.viewDate.getUTCFullYear(),
                                l = this.viewDate.getUTCMonth(),
                                c = this.viewDate.getUTCDate(),
                                u = this.viewDate.getUTCHours(),
                                d = this.viewDate.getUTCMinutes(),
                                h = this.viewDate.getUTCSeconds();
                            if (n.is(".month") ? (this.viewDate.setUTCDate(1), l = n.parent().find("span").index(n), c = this.viewDate.getUTCDate(), this.viewDate.setUTCMonth(l), this.element.trigger({
                                type: "changeMonth",
                                date: this.viewDate
                            }), this.viewSelect >= 3 && this._setDate(i(r, l, c, u, d, h, 0))) : n.is(".year") ? (this.viewDate.setUTCDate(1), r = parseInt(n.text(), 10) || 0, this.viewDate.setUTCFullYear(r), this.element.trigger({
                                type: "changeYear",
                                date: this.viewDate
                            }), this.viewSelect >= 4 && this._setDate(i(r, l, c, u, d, h, 0))) : n.is(".hour") ? (u = parseInt(n.text(), 10) || 0, (n.hasClass("hour_am") || n.hasClass("hour_pm")) && (12 === u && n.hasClass("hour_am") ? u = 0 : 12 !== u && n.hasClass("hour_pm") && (u += 12)), this.viewDate.setUTCHours(u), this.element.trigger({
                                type: "changeHour",
                                date: this.viewDate
                            }), this.viewSelect >= 1 && this._setDate(i(r, l, c, u, d, h, 0))) : n.is(".minute") && (d = parseInt(n.text().substr(n.text().indexOf(":") + 1), 10) || 0, this.viewDate.setUTCMinutes(d), this.element.trigger({
                                type: "changeMinute",
                                date: this.viewDate
                            }), this.viewSelect >= 0 && this._setDate(i(r, l, c, u, d, h, 0))), 0 !== this.viewMode) {
                                var f = this.viewMode;
                                this.showMode(-1), this.fill(), f === this.viewMode && this.autoclose && this.hide()
                            } else this.fill(), this.autoclose && this.hide()
                        }
                        break;
                    case "td":
                        if (n.is(".day") && !n.is(".disabled")) {
                            var c = parseInt(n.text(), 10) || 1,
                                r = this.viewDate.getUTCFullYear(),
                                l = this.viewDate.getUTCMonth(),
                                u = this.viewDate.getUTCHours(),
                                d = this.viewDate.getUTCMinutes(),
                                h = this.viewDate.getUTCSeconds();
                            n.is(".old") ? 0 === l ? (l = 11, r -= 1) : l -= 1 : n.is(".new") && (11 === l ? (l = 0, r += 1) : l += 1), this.viewDate.setUTCFullYear(r), this.viewDate.setUTCMonth(l, c), this.element.trigger({
                                type: "changeDay",
                                date: this.viewDate
                            }), this.viewSelect >= 2 && this._setDate(i(r, l, c, u, d, h, 0))
                        }
                        var f = this.viewMode;
                        this.showMode(-1), this.fill(), f === this.viewMode && this.autoclose && this.hide()
                }
            }
        },
        _setDate: function(e, t) {
            t && "date" !== t || (this.date = e), t && "view" !== t || (this.viewDate = e), this.fill(), this.setValue();
            var n;
            this.isInput ? n = this.element : this.component && (n = this.element.find("input")), n && n.change(), this.element.trigger({
                type: "changeDate",
                date: this.getDate()
            }), null === e && (this.date = this.viewDate)
        },
        moveMinute: function(e, t) {
            if (!t) return e;
            var n = new Date(e.valueOf());
            return n.setUTCMinutes(n.getUTCMinutes() + t * this.minuteStep), n
        },
        moveHour: function(e, t) {
            if (!t) return e;
            var n = new Date(e.valueOf());
            return n.setUTCHours(n.getUTCHours() + t), n
        },
        moveDate: function(e, t) {
            if (!t) return e;
            var n = new Date(e.valueOf());
            return n.setUTCDate(n.getUTCDate() + t), n
        },
        moveMonth: function(e, t) {
            if (!t) return e;
            var n, i, o = new Date(e.valueOf()),
                s = o.getUTCDate(),
                r = o.getUTCMonth(),
                a = Math.abs(t);
            if (t = t > 0 ? 1 : -1, 1 === a) i = t === -1 ? function() {
                return o.getUTCMonth() === r
            } : function() {
                return o.getUTCMonth() !== n
            }, n = r + t, o.setUTCMonth(n), (n < 0 || n > 11) && (n = (n + 12) % 12);
            else {
                for (var l = 0; l < a; l++) o = this.moveMonth(o, t);
                n = o.getUTCMonth(), o.setUTCDate(s), i = function() {
                    return n !== o.getUTCMonth()
                }
            }
            for (; i();) o.setUTCDate(--s), o.setUTCMonth(n);
            return o
        },
        moveYear: function(e, t) {
            return this.moveMonth(e, 12 * t)
        },
        dateWithinRange: function(e) {
            return e >= this.startDate && e <= this.endDate
        },
        keydown: function(e) {
            if (this.picker.is(":not(:visible)")) return void(27 === e.keyCode && this.show());
            var t, n, i, o = !1;
            switch (e.keyCode) {
                case 27:
                    this.hide(), e.preventDefault();
                    break;
                case 37:
                case 39:
                    if (!this.keyboardNavigation) break;
                    t = 37 === e.keyCode ? -1 : 1;
                    var s = this.viewMode;
                    e.ctrlKey ? s += 2 : e.shiftKey && (s += 1), 4 === s ? (n = this.moveYear(this.date, t), i = this.moveYear(this.viewDate, t)) : 3 === s ? (n = this.moveMonth(this.date, t), i = this.moveMonth(this.viewDate, t)) : 2 === s ? (n = this.moveDate(this.date, t), i = this.moveDate(this.viewDate, t)) : 1 === s ? (n = this.moveHour(this.date, t), i = this.moveHour(this.viewDate, t)) : 0 === s && (n = this.moveMinute(this.date, t), i = this.moveMinute(this.viewDate, t)), this.dateWithinRange(n) && (this.date = n, this.viewDate = i, this.setValue(), this.update(), e.preventDefault(), o = !0);
                    break;
                case 38:
                case 40:
                    if (!this.keyboardNavigation) break;
                    t = 38 === e.keyCode ? -1 : 1, s = this.viewMode, e.ctrlKey ? s += 2 : e.shiftKey && (s += 1), 4 === s ? (n = this.moveYear(this.date, t), i = this.moveYear(this.viewDate, t)) : 3 === s ? (n = this.moveMonth(this.date, t), i = this.moveMonth(this.viewDate, t)) : 2 === s ? (n = this.moveDate(this.date, 7 * t), i = this.moveDate(this.viewDate, 7 * t)) : 1 === s ? this.showMeridian ? (n = this.moveHour(this.date, 6 * t), i = this.moveHour(this.viewDate, 6 * t)) : (n = this.moveHour(this.date, 4 * t), i = this.moveHour(this.viewDate, 4 * t)) : 0 === s && (n = this.moveMinute(this.date, 4 * t), i = this.moveMinute(this.viewDate, 4 * t)), this.dateWithinRange(n) && (this.date = n, this.viewDate = i, this.setValue(), this.update(), e.preventDefault(), o = !0);
                    break;
                case 13:
                    if (0 !== this.viewMode) {
                        var r = this.viewMode;
                        this.showMode(-1), this.fill(), r === this.viewMode && this.autoclose && this.hide()
                    } else this.fill(), this.autoclose && this.hide();
                    e.preventDefault();
                    break;
                case 9:
                    this.hide()
            }
            if (o) {
                var a;
                this.isInput ? a = this.element : this.component && (a = this.element.find("input")), a && a.change(), this.element.trigger({
                    type: "changeDate",
                    date: this.getDate()
                })
            }
        },
        showMode: function(e) {
            if (e) {
                var t = Math.max(0, Math.min(a.modes.length - 1, this.viewMode + e));
                t >= this.minView && t <= this.maxView && (this.element.trigger({
                    type: "changeMode",
                    date: this.viewDate,
                    oldViewMode: this.viewMode,
                    newViewMode: t
                }), this.viewMode = t)
            }
            this.picker.find(">div").hide().filter(".datetimepicker-" + a.modes[this.viewMode].clsName).css("display", "block"), this.updateNavArrows()
        },
        reset: function() {
            this._setDate(null, "date")
        },
        convertViewModeText: function(e) {
            switch (e) {
                case 4:
                    return "decade";
                case 3:
                    return "year";
                case 2:
                    return "month";
                case 1:
                    return "day";
                case 0:
                    return "hour"
            }
        }
    };
    var s = e.fn.datetimepicker;
    e.fn.datetimepicker = function(n) {
        var i = Array.apply(null, arguments);
        i.shift();
        var s;
        return this.each(function() {
            var r = e(this),
                a = r.data("datetimepicker"),
                l = "object" == typeof n && n;
            if (a || r.data("datetimepicker", a = new o(this, e.extend({}, e.fn.datetimepicker.defaults, l))), "string" == typeof n && "function" == typeof a[n] && (s = a[n].apply(a, i), s !== t)) return !1
        }), s !== t ? s : this
    }, e.fn.datetimepicker.defaults = {}, e.fn.datetimepicker.Constructor = o;
    var r = e.fn.datetimepicker.dates = {
            en: {
                days: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
                daysShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
                daysMin: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"],
                months: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
                monthsShort: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
                meridiem: ["am", "pm"],
                suffix: ["st", "nd", "rd", "th"],
                today: "Today",
                clear: "Clear"
            }
        },
        a = {
            modes: [{
                clsName: "minutes",
                navFnc: "Hours",
                navStep: 1
            }, {
                clsName: "hours",
                navFnc: "Date",
                navStep: 1
            }, {
                clsName: "days",
                navFnc: "Month",
                navStep: 1
            }, {
                clsName: "months",
                navFnc: "FullYear",
                navStep: 1
            }, {
                clsName: "years",
                navFnc: "FullYear",
                navStep: 10
            }],
            isLeapYear: function(e) {
                return e % 4 === 0 && e % 100 !== 0 || e % 400 === 0
            },
            getDaysInMonth: function(e, t) {
                return [31, a.isLeapYear(e) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][t]
            },
            getDefaultFormat: function(e, t) {
                if ("standard" === e) return "input" === t ? "yyyy-mm-dd hh:ii" : "yyyy-mm-dd hh:ii:ss";
                if ("php" === e) return "input" === t ? "Y-m-d H:i" : "Y-m-d H:i:s";
                throw new Error("Invalid format type.")
            },
            validParts: function(e) {
                if ("standard" === e) return /t|hh?|HH?|p|P|z|Z|ii?|ss?|dd?|DD?|mm?|MM?|yy(?:yy)?/g;
                if ("php" === e) return /[dDjlNwzFmMnStyYaABgGhHis]/g;
                throw new Error("Invalid format type.")
            },
            nonpunctuation: /[^ -\/:-@\[-`{-~\t\n\rTZ]+/g,
            parseFormat: function(e, t) {
                var n = e.replace(this.validParts(t), "\0").split("\0"),
                    i = e.match(this.validParts(t));
                if (!n || !n.length || !i || 0 === i.length) throw new Error("Invalid date format.");
                return {
                    separators: n,
                    parts: i
                }
            },
            parseDate: function(t, n, s, a, l) {
                if (t instanceof Date) {
                    var c = new Date(t.valueOf() - 6e4 * t.getTimezoneOffset());
                    return c.setMilliseconds(0), c
                }
                if (/^\d{4}\-\d{1,2}\-\d{1,2}$/.test(t) && (n = this.parseFormat("yyyy-mm-dd", a)), /^\d{4}\-\d{1,2}\-\d{1,2}[T ]\d{1,2}\:\d{1,2}$/.test(t) && (n = this.parseFormat("yyyy-mm-dd hh:ii", a)), /^\d{4}\-\d{1,2}\-\d{1,2}[T ]\d{1,2}\:\d{1,2}\:\d{1,2}[Z]{0,1}$/.test(t) && (n = this.parseFormat("yyyy-mm-dd hh:ii:ss", a)), /^[-+]\d+[dmwy]([\s,]+[-+]\d+[dmwy])*$/.test(t)) {
                    var u, d, h = /([-+]\d+)([dmwy])/,
                        f = t.match(/([-+]\d+)([dmwy])/g);
                    t = new Date;
                    for (var p = 0; p < f.length; p++) switch (u = h.exec(f[p]), d = parseInt(u[1]), u[2]) {
                        case "d":
                            t.setUTCDate(t.getUTCDate() + d);
                            break;
                        case "m":
                            t = o.prototype.moveMonth.call(o.prototype, t, d);
                            break;
                        case "w":
                            t.setUTCDate(t.getUTCDate() + 7 * d);
                            break;
                        case "y":
                            t = o.prototype.moveYear.call(o.prototype, t, d)
                    }
                    return i(t.getUTCFullYear(), t.getUTCMonth(), t.getUTCDate(), t.getUTCHours(), t.getUTCMinutes(), t.getUTCSeconds(), 0)
                }
                var m, g, u, f = t && t.toString().match(this.nonpunctuation) || [],
                    t = new Date(0, 0, 0, 0, 0, 0, 0),
                    v = {},
                    y = ["hh", "h", "ii", "i", "ss", "s", "yyyy", "yy", "M", "MM", "m", "mm", "D", "DD", "d", "dd", "H", "HH", "p", "P", "z", "Z"],
                    b = {
                        hh: function(e, t) {
                            return e.setUTCHours(t)
                        },
                        h: function(e, t) {
                            return e.setUTCHours(t)
                        },
                        HH: function(e, t) {
                            return e.setUTCHours(12 === t ? 0 : t)
                        },
                        H: function(e, t) {
                            return e.setUTCHours(12 === t ? 0 : t)
                        },
                        ii: function(e, t) {
                            return e.setUTCMinutes(t)
                        },
                        i: function(e, t) {
                            return e.setUTCMinutes(t)
                        },
                        ss: function(e, t) {
                            return e.setUTCSeconds(t)
                        },
                        s: function(e, t) {
                            return e.setUTCSeconds(t)
                        },
                        yyyy: function(e, t) {
                            return e.setUTCFullYear(t)
                        },
                        yy: function(e, t) {
                            return e.setUTCFullYear(2e3 + t)
                        },
                        m: function(e, t) {
                            for (t -= 1; t < 0;) t += 12;
                            for (t %= 12, e.setUTCMonth(t); e.getUTCMonth() !== t;) {
                                if (isNaN(e.getUTCMonth())) return e;
                                e.setUTCDate(e.getUTCDate() - 1)
                            }
                            return e
                        },
                        d: function(e, t) {
                            return e.setUTCDate(t)
                        },
                        p: function(e, t) {
                            return e.setUTCHours(1 === t ? e.getUTCHours() + 12 : e.getUTCHours())
                        },
                        z: function() {
                            return l
                        }
                    };
                if (b.M = b.MM = b.mm = b.m, b.dd = b.d, b.P = b.p, b.Z = b.z, t = i(t.getFullYear(), t.getMonth(), t.getDate(), t.getHours(), t.getMinutes(), t.getSeconds()), f.length === n.parts.length) {
                    for (var p = 0, w = n.parts.length; p < w; p++) {
                        if (m = parseInt(f[p], 10), u = n.parts[p], isNaN(m)) switch (u) {
                            case "MM":
                                g = e(r[s].months).filter(function() {
                                    var e = this.slice(0, f[p].length),
                                        t = f[p].slice(0, e.length);
                                    return e === t
                                }), m = e.inArray(g[0], r[s].months) + 1;
                                break;
                            case "M":
                                g = e(r[s].monthsShort).filter(function() {
                                    var e = this.slice(0, f[p].length),
                                        t = f[p].slice(0, e.length);
                                    return e.toLowerCase() === t.toLowerCase()
                                }), m = e.inArray(g[0], r[s].monthsShort) + 1;
                                break;
                            case "p":
                            case "P":
                                m = e.inArray(f[p].toLowerCase(), r[s].meridiem);
                                break;
                            case "z":
                            case "Z":
                        }
                        v[u] = m
                    }
                    for (var T, p = 0; p < y.length; p++) T = y[p], T in v && !isNaN(v[T]) && b[T](t, v[T])
                }
                return t
            },
            formatDate: function(t, n, i, o, s) {
                if (null === t) return "";
                var l;
                if ("standard" === o) l = {
                    t: t.getTime(),
                    yy: t.getUTCFullYear().toString().substring(2),
                    yyyy: t.getUTCFullYear(),
                    m: t.getUTCMonth() + 1,
                    M: r[i].monthsShort[t.getUTCMonth()],
                    MM: r[i].months[t.getUTCMonth()],
                    d: t.getUTCDate(),
                    D: r[i].daysShort[t.getUTCDay()],
                    DD: r[i].days[t.getUTCDay()],
                    p: 2 === r[i].meridiem.length ? r[i].meridiem[t.getUTCHours() < 12 ? 0 : 1] : "",
                    h: t.getUTCHours(),
                    i: t.getUTCMinutes(),
                    s: t.getUTCSeconds(),
                    z: s
                }, 2 === r[i].meridiem.length ? l.H = l.h % 12 === 0 ? 12 : l.h % 12 : l.H = l.h, l.HH = (l.H < 10 ? "0" : "") + l.H, l.P = l.p.toUpperCase(), l.Z = l.z, l.hh = (l.h < 10 ? "0" : "") + l.h, l.ii = (l.i < 10 ? "0" : "") + l.i, l.ss = (l.s < 10 ? "0" : "") + l.s, l.dd = (l.d < 10 ? "0" : "") + l.d, l.mm = (l.m < 10 ? "0" : "") + l.m;
                else {
                    if ("php" !== o) throw new Error("Invalid format type.");
                    l = {
                        y: t.getUTCFullYear().toString().substring(2),
                        Y: t.getUTCFullYear(),
                        F: r[i].months[t.getUTCMonth()],
                        M: r[i].monthsShort[t.getUTCMonth()],
                        n: t.getUTCMonth() + 1,
                        t: a.getDaysInMonth(t.getUTCFullYear(), t.getUTCMonth()),
                        j: t.getUTCDate(),
                        l: r[i].days[t.getUTCDay()],
                        D: r[i].daysShort[t.getUTCDay()],
                        w: t.getUTCDay(),
                        N: 0 === t.getUTCDay() ? 7 : t.getUTCDay(),
                        S: t.getUTCDate() % 10 <= r[i].suffix.length ? r[i].suffix[t.getUTCDate() % 10 - 1] : "",
                        a: 2 === r[i].meridiem.length ? r[i].meridiem[t.getUTCHours() < 12 ? 0 : 1] : "",
                        g: t.getUTCHours() % 12 === 0 ? 12 : t.getUTCHours() % 12,
                        G: t.getUTCHours(),
                        i: t.getUTCMinutes(),
                        s: t.getUTCSeconds()
                    }, l.m = (l.n < 10 ? "0" : "") + l.n, l.d = (l.j < 10 ? "0" : "") + l.j, l.A = l.a.toString().toUpperCase(), l.h = (l.g < 10 ? "0" : "") + l.g, l.H = (l.G < 10 ? "0" : "") + l.G, l.i = (l.i < 10 ? "0" : "") + l.i, l.s = (l.s < 10 ? "0" : "") + l.s
                }
                for (var t = [], c = e.extend([], n.separators), u = 0, d = n.parts.length; u < d; u++) c.length && t.push(c.shift()), t.push(l[n.parts[u]]);
                return c.length && t.push(c.shift()), t.join("")
            },
            convertViewMode: function(e) {
                switch (e) {
                    case 4:
                    case "decade":
                        e = 4;
                        break;
                    case 3:
                    case "year":
                        e = 3;
                        break;
                    case 2:
                    case "month":
                        e = 2;
                        break;
                    case 1:
                    case "day":
                        e = 1;
                        break;
                    case 0:
                    case "hour":
                        e = 0
                }
                return e
            },
            headTemplate: '<thead><tr><th class="prev"><i class="{iconType} {leftArrow}"/></th><th colspan="5" class="switch"></th><th class="next"><i class="{iconType} {rightArrow}"/></th></tr></thead>',
            headTemplateV3: '<thead><tr><th class="prev"><span class="{iconType} {leftArrow}"></span> </th><th colspan="5" class="switch"></th><th class="next"><span class="{iconType} {rightArrow}"></span> </th></tr></thead>',
            contTemplate: '<tbody><tr><td colspan="7"></td></tr></tbody>',
            footTemplate: '<tfoot><tr><th colspan="7" class="today"></th></tr><tr><th colspan="7" class="clear"></th></tr></tfoot>'
        };
    a.template = '<div class="datetimepicker"><div class="datetimepicker-minutes"><table class=" table-condensed">' + a.headTemplate + a.contTemplate + a.footTemplate + '</table></div><div class="datetimepicker-hours"><table class=" table-condensed">' + a.headTemplate + a.contTemplate + a.footTemplate + '</table></div><div class="datetimepicker-days"><table class=" table-condensed">' + a.headTemplate + "<tbody></tbody>" + a.footTemplate + '</table></div><div class="datetimepicker-months"><table class="table-condensed">' + a.headTemplate + a.contTemplate + a.footTemplate + '</table></div><div class="datetimepicker-years"><table class="table-condensed">' + a.headTemplate + a.contTemplate + a.footTemplate + "</table></div></div>", a.templateV3 = '<div class="datetimepicker"><div class="datetimepicker-minutes"><table class=" table-condensed">' + a.headTemplateV3 + a.contTemplate + a.footTemplate + '</table></div><div class="datetimepicker-hours"><table class=" table-condensed">' + a.headTemplateV3 + a.contTemplate + a.footTemplate + '</table></div><div class="datetimepicker-days"><table class=" table-condensed">' + a.headTemplateV3 + "<tbody></tbody>" + a.footTemplate + '</table></div><div class="datetimepicker-months"><table class="table-condensed">' + a.headTemplateV3 + a.contTemplate + a.footTemplate + '</table></div><div class="datetimepicker-years"><table class="table-condensed">' + a.headTemplateV3 + a.contTemplate + a.footTemplate + "</table></div></div>", e.fn.datetimepicker.DPGlobal = a, e.fn.datetimepicker.noConflict = function() {
        return e.fn.datetimepicker = s, this
    }, e(document).on("focus.datetimepicker.data-api click.datetimepicker.data-api", '[data-provide="datetimepicker"]', function(t) {
        var n = e(this);
        n.data("datetimepicker") || (t.preventDefault(), n.datetimepicker("show"))
    }), e(function() {
        e('[data-provide="datetimepicker-inline"]').datetimepicker()
    })
}),
function(e) {
    e.fn.datetimepicker.dates["zh-CN"] = {
        days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
        daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
        daysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
        months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        today: "今天",
        suffix: [],
        meridiem: ["上午", "下午"]
    }
}(jQuery);