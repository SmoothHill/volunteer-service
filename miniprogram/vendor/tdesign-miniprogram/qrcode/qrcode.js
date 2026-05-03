var __create = Object.create;
var __defProp = Object.defineProperty;
var __getOwnPropDesc = Object.getOwnPropertyDescriptor;
var __getOwnPropNames = Object.getOwnPropertyNames;
var __getProtoOf = Object.getPrototypeOf;
var __hasOwnProp = Object.prototype.hasOwnProperty;
var __commonJS = (cb, mod) => function __require() {
  return mod || (0, cb[__getOwnPropNames(cb)[0]])((mod = { exports: {} }).exports, mod), mod.exports;
};
var __export = (target, all) => {
  for (var name in all)
    __defProp(target, name, { get: all[name], enumerable: true });
};
var __copyProps = (to, from, except, desc) => {
  if (from && typeof from === "object" || typeof from === "function") {
    for (let key of __getOwnPropNames(from))
      if (!__hasOwnProp.call(to, key) && key !== except)
        __defProp(to, key, { get: () => from[key], enumerable: !(desc = __getOwnPropDesc(from, key)) || desc.enumerable });
  }
  return to;
};
var __toESM = (mod, isNodeMode, target) => (target = mod != null ? __create(__getProtoOf(mod)) : {}, __copyProps(
  // If the importer is in node compatibility mode or this is not an ESM
  // file that has been converted to a CommonJS file using a Babel-
  // compatible transform (i.e. "__esModule" has not been set), then set
  // "default" to the CommonJS "module.exports" for node compatibility.
  isNodeMode || !mod || !mod.__esModule ? __defProp(target, "default", { value: mod, enumerable: true }) : target,
  mod
));
var __toCommonJS = (mod) => __copyProps(__defProp({}, "__esModule", { value: true }), mod);

// node_modules/dayjs/dayjs.min.js
var require_dayjs_min = __commonJS({
  "node_modules/dayjs/dayjs.min.js"(exports, module2) {
    !(function(t, e) {
      "object" == typeof exports && "undefined" != typeof module2 ? module2.exports = e() : "function" == typeof define && define.amd ? define(e) : (t = "undefined" != typeof globalThis ? globalThis : t || self).dayjs = e();
    })(exports, (function() {
      "use strict";
      var t = 1e3, e = 6e4, n = 36e5, r = "millisecond", i = "second", s = "minute", u = "hour", a = "day", o = "week", c = "month", f = "quarter", h = "year", d = "date", l = "Invalid Date", $ = /^(\d{4})[-/]?(\d{1,2})?[-/]?(\d{0,2})[Tt\s]*(\d{1,2})?:?(\d{1,2})?:?(\d{1,2})?[.:]?(\d+)?$/, y = /\[([^\]]+)]|Y{1,4}|M{1,4}|D{1,2}|d{1,4}|H{1,2}|h{1,2}|a|A|m{1,2}|s{1,2}|Z{1,2}|SSS/g, M = { name: "en", weekdays: "Sunday_Monday_Tuesday_Wednesday_Thursday_Friday_Saturday".split("_"), months: "January_February_March_April_May_June_July_August_September_October_November_December".split("_"), ordinal: function(t2) {
        var e2 = ["th", "st", "nd", "rd"], n2 = t2 % 100;
        return "[" + t2 + (e2[(n2 - 20) % 10] || e2[n2] || e2[0]) + "]";
      } }, m = function(t2, e2, n2) {
        var r2 = String(t2);
        return !r2 || r2.length >= e2 ? t2 : "" + Array(e2 + 1 - r2.length).join(n2) + t2;
      }, v = { s: m, z: function(t2) {
        var e2 = -t2.utcOffset(), n2 = Math.abs(e2), r2 = Math.floor(n2 / 60), i2 = n2 % 60;
        return (e2 <= 0 ? "+" : "-") + m(r2, 2, "0") + ":" + m(i2, 2, "0");
      }, m: function t2(e2, n2) {
        if (e2.date() < n2.date()) return -t2(n2, e2);
        var r2 = 12 * (n2.year() - e2.year()) + (n2.month() - e2.month()), i2 = e2.clone().add(r2, c), s2 = n2 - i2 < 0, u2 = e2.clone().add(r2 + (s2 ? -1 : 1), c);
        return +(-(r2 + (n2 - i2) / (s2 ? i2 - u2 : u2 - i2)) || 0);
      }, a: function(t2) {
        return t2 < 0 ? Math.ceil(t2) || 0 : Math.floor(t2);
      }, p: function(t2) {
        return { M: c, y: h, w: o, d: a, D: d, h: u, m: s, s: i, ms: r, Q: f }[t2] || String(t2 || "").toLowerCase().replace(/s$/, "");
      }, u: function(t2) {
        return void 0 === t2;
      } }, g = "en", D = {};
      D[g] = M;
      var p = "$isDayjsObject", S = function(t2) {
        return t2 instanceof _ || !(!t2 || !t2[p]);
      }, w = function t2(e2, n2, r2) {
        var i2;
        if (!e2) return g;
        if ("string" == typeof e2) {
          var s2 = e2.toLowerCase();
          D[s2] && (i2 = s2), n2 && (D[s2] = n2, i2 = s2);
          var u2 = e2.split("-");
          if (!i2 && u2.length > 1) return t2(u2[0]);
        } else {
          var a2 = e2.name;
          D[a2] = e2, i2 = a2;
        }
        return !r2 && i2 && (g = i2), i2 || !r2 && g;
      }, O = function(t2, e2) {
        if (S(t2)) return t2.clone();
        var n2 = "object" == typeof e2 ? e2 : {};
        return n2.date = t2, n2.args = arguments, new _(n2);
      }, b = v;
      b.l = w, b.i = S, b.w = function(t2, e2) {
        return O(t2, { locale: e2.$L, utc: e2.$u, x: e2.$x, $offset: e2.$offset });
      };
      var _ = (function() {
        function M2(t2) {
          this.$L = w(t2.locale, null, true), this.parse(t2), this.$x = this.$x || t2.x || {}, this[p] = true;
        }
        var m2 = M2.prototype;
        return m2.parse = function(t2) {
          this.$d = (function(t3) {
            var e2 = t3.date, n2 = t3.utc;
            if (null === e2) return /* @__PURE__ */ new Date(NaN);
            if (b.u(e2)) return /* @__PURE__ */ new Date();
            if (e2 instanceof Date) return new Date(e2);
            if ("string" == typeof e2 && !/Z$/i.test(e2)) {
              var r2 = e2.match($);
              if (r2) {
                var i2 = r2[2] - 1 || 0, s2 = (r2[7] || "0").substring(0, 3);
                return n2 ? new Date(Date.UTC(r2[1], i2, r2[3] || 1, r2[4] || 0, r2[5] || 0, r2[6] || 0, s2)) : new Date(r2[1], i2, r2[3] || 1, r2[4] || 0, r2[5] || 0, r2[6] || 0, s2);
              }
            }
            return new Date(e2);
          })(t2), this.init();
        }, m2.init = function() {
          var t2 = this.$d;
          this.$y = t2.getFullYear(), this.$M = t2.getMonth(), this.$D = t2.getDate(), this.$W = t2.getDay(), this.$H = t2.getHours(), this.$m = t2.getMinutes(), this.$s = t2.getSeconds(), this.$ms = t2.getMilliseconds();
        }, m2.$utils = function() {
          return b;
        }, m2.isValid = function() {
          return !(this.$d.toString() === l);
        }, m2.isSame = function(t2, e2) {
          var n2 = O(t2);
          return this.startOf(e2) <= n2 && n2 <= this.endOf(e2);
        }, m2.isAfter = function(t2, e2) {
          return O(t2) < this.startOf(e2);
        }, m2.isBefore = function(t2, e2) {
          return this.endOf(e2) < O(t2);
        }, m2.$g = function(t2, e2, n2) {
          return b.u(t2) ? this[e2] : this.set(n2, t2);
        }, m2.unix = function() {
          return Math.floor(this.valueOf() / 1e3);
        }, m2.valueOf = function() {
          return this.$d.getTime();
        }, m2.startOf = function(t2, e2) {
          var n2 = this, r2 = !!b.u(e2) || e2, f2 = b.p(t2), l2 = function(t3, e3) {
            var i2 = b.w(n2.$u ? Date.UTC(n2.$y, e3, t3) : new Date(n2.$y, e3, t3), n2);
            return r2 ? i2 : i2.endOf(a);
          }, $2 = function(t3, e3) {
            return b.w(n2.toDate()[t3].apply(n2.toDate("s"), (r2 ? [0, 0, 0, 0] : [23, 59, 59, 999]).slice(e3)), n2);
          }, y2 = this.$W, M3 = this.$M, m3 = this.$D, v2 = "set" + (this.$u ? "UTC" : "");
          switch (f2) {
            case h:
              return r2 ? l2(1, 0) : l2(31, 11);
            case c:
              return r2 ? l2(1, M3) : l2(0, M3 + 1);
            case o:
              var g2 = this.$locale().weekStart || 0, D2 = (y2 < g2 ? y2 + 7 : y2) - g2;
              return l2(r2 ? m3 - D2 : m3 + (6 - D2), M3);
            case a:
            case d:
              return $2(v2 + "Hours", 0);
            case u:
              return $2(v2 + "Minutes", 1);
            case s:
              return $2(v2 + "Seconds", 2);
            case i:
              return $2(v2 + "Milliseconds", 3);
            default:
              return this.clone();
          }
        }, m2.endOf = function(t2) {
          return this.startOf(t2, false);
        }, m2.$set = function(t2, e2) {
          var n2, o2 = b.p(t2), f2 = "set" + (this.$u ? "UTC" : ""), l2 = (n2 = {}, n2[a] = f2 + "Date", n2[d] = f2 + "Date", n2[c] = f2 + "Month", n2[h] = f2 + "FullYear", n2[u] = f2 + "Hours", n2[s] = f2 + "Minutes", n2[i] = f2 + "Seconds", n2[r] = f2 + "Milliseconds", n2)[o2], $2 = o2 === a ? this.$D + (e2 - this.$W) : e2;
          if (o2 === c || o2 === h) {
            var y2 = this.clone().set(d, 1);
            y2.$d[l2]($2), y2.init(), this.$d = y2.set(d, Math.min(this.$D, y2.daysInMonth())).$d;
          } else l2 && this.$d[l2]($2);
          return this.init(), this;
        }, m2.set = function(t2, e2) {
          return this.clone().$set(t2, e2);
        }, m2.get = function(t2) {
          return this[b.p(t2)]();
        }, m2.add = function(r2, f2) {
          var d2, l2 = this;
          r2 = Number(r2);
          var $2 = b.p(f2), y2 = function(t2) {
            var e2 = O(l2);
            return b.w(e2.date(e2.date() + Math.round(t2 * r2)), l2);
          };
          if ($2 === c) return this.set(c, this.$M + r2);
          if ($2 === h) return this.set(h, this.$y + r2);
          if ($2 === a) return y2(1);
          if ($2 === o) return y2(7);
          var M3 = (d2 = {}, d2[s] = e, d2[u] = n, d2[i] = t, d2)[$2] || 1, m3 = this.$d.getTime() + r2 * M3;
          return b.w(m3, this);
        }, m2.subtract = function(t2, e2) {
          return this.add(-1 * t2, e2);
        }, m2.format = function(t2) {
          var e2 = this, n2 = this.$locale();
          if (!this.isValid()) return n2.invalidDate || l;
          var r2 = t2 || "YYYY-MM-DDTHH:mm:ssZ", i2 = b.z(this), s2 = this.$H, u2 = this.$m, a2 = this.$M, o2 = n2.weekdays, c2 = n2.months, f2 = n2.meridiem, h2 = function(t3, n3, i3, s3) {
            return t3 && (t3[n3] || t3(e2, r2)) || i3[n3].slice(0, s3);
          }, d2 = function(t3) {
            return b.s(s2 % 12 || 12, t3, "0");
          }, $2 = f2 || function(t3, e3, n3) {
            var r3 = t3 < 12 ? "AM" : "PM";
            return n3 ? r3.toLowerCase() : r3;
          };
          return r2.replace(y, (function(t3, r3) {
            return r3 || (function(t4) {
              switch (t4) {
                case "YY":
                  return String(e2.$y).slice(-2);
                case "YYYY":
                  return b.s(e2.$y, 4, "0");
                case "M":
                  return a2 + 1;
                case "MM":
                  return b.s(a2 + 1, 2, "0");
                case "MMM":
                  return h2(n2.monthsShort, a2, c2, 3);
                case "MMMM":
                  return h2(c2, a2);
                case "D":
                  return e2.$D;
                case "DD":
                  return b.s(e2.$D, 2, "0");
                case "d":
                  return String(e2.$W);
                case "dd":
                  return h2(n2.weekdaysMin, e2.$W, o2, 2);
                case "ddd":
                  return h2(n2.weekdaysShort, e2.$W, o2, 3);
                case "dddd":
                  return o2[e2.$W];
                case "H":
                  return String(s2);
                case "HH":
                  return b.s(s2, 2, "0");
                case "h":
                  return d2(1);
                case "hh":
                  return d2(2);
                case "a":
                  return $2(s2, u2, true);
                case "A":
                  return $2(s2, u2, false);
                case "m":
                  return String(u2);
                case "mm":
                  return b.s(u2, 2, "0");
                case "s":
                  return String(e2.$s);
                case "ss":
                  return b.s(e2.$s, 2, "0");
                case "SSS":
                  return b.s(e2.$ms, 3, "0");
                case "Z":
                  return i2;
              }
              return null;
            })(t3) || i2.replace(":", "");
          }));
        }, m2.utcOffset = function() {
          return 15 * -Math.round(this.$d.getTimezoneOffset() / 15);
        }, m2.diff = function(r2, d2, l2) {
          var $2, y2 = this, M3 = b.p(d2), m3 = O(r2), v2 = (m3.utcOffset() - this.utcOffset()) * e, g2 = this - m3, D2 = function() {
            return b.m(y2, m3);
          };
          switch (M3) {
            case h:
              $2 = D2() / 12;
              break;
            case c:
              $2 = D2();
              break;
            case f:
              $2 = D2() / 3;
              break;
            case o:
              $2 = (g2 - v2) / 6048e5;
              break;
            case a:
              $2 = (g2 - v2) / 864e5;
              break;
            case u:
              $2 = g2 / n;
              break;
            case s:
              $2 = g2 / e;
              break;
            case i:
              $2 = g2 / t;
              break;
            default:
              $2 = g2;
          }
          return l2 ? $2 : b.a($2);
        }, m2.daysInMonth = function() {
          return this.endOf(c).$D;
        }, m2.$locale = function() {
          return D[this.$L];
        }, m2.locale = function(t2, e2) {
          if (!t2) return this.$L;
          var n2 = this.clone(), r2 = w(t2, e2, true);
          return r2 && (n2.$L = r2), n2;
        }, m2.clone = function() {
          return b.w(this.$d, this);
        }, m2.toDate = function() {
          return new Date(this.valueOf());
        }, m2.toJSON = function() {
          return this.isValid() ? this.toISOString() : null;
        }, m2.toISOString = function() {
          return this.$d.toISOString();
        }, m2.toString = function() {
          return this.$d.toUTCString();
        }, M2;
      })(), k = _.prototype;
      return O.prototype = k, [["$ms", r], ["$s", i], ["$m", s], ["$H", u], ["$W", a], ["$M", c], ["$y", h], ["$D", d]].forEach((function(t2) {
        k[t2[1]] = function(e2) {
          return this.$g(e2, t2[0], t2[1]);
        };
      })), O.extend = function(t2, e2) {
        return t2.$i || (t2(e2, _, O), t2.$i = true), O;
      }, O.locale = w, O.isDayjs = S, O.unix = function(t2) {
        return O(1e3 * t2);
      }, O.en = D[g], O.Ls = D, O.p = {}, O;
    }));
  }
});

// node_modules/dayjs/locale/zh-cn.js
var require_zh_cn = __commonJS({
  "node_modules/dayjs/locale/zh-cn.js"(exports, module2) {
    !(function(e, _) {
      "object" == typeof exports && "undefined" != typeof module2 ? module2.exports = _(require_dayjs_min()) : "function" == typeof define && define.amd ? define(["dayjs"], _) : (e = "undefined" != typeof globalThis ? globalThis : e || self).dayjs_locale_zh_cn = _(e.dayjs);
    })(exports, (function(e) {
      "use strict";
      function _(e2) {
        return e2 && "object" == typeof e2 && "default" in e2 ? e2 : { default: e2 };
      }
      var t = _(e), d = { name: "zh-cn", weekdays: "星期日_星期一_星期二_星期三_星期四_星期五_星期六".split("_"), weekdaysShort: "周日_周一_周二_周三_周四_周五_周六".split("_"), weekdaysMin: "日_一_二_三_四_五_六".split("_"), months: "一月_二月_三月_四月_五月_六月_七月_八月_九月_十月_十一月_十二月".split("_"), monthsShort: "1月_2月_3月_4月_5月_6月_7月_8月_9月_10月_11月_12月".split("_"), ordinal: function(e2, _2) {
        return "W" === _2 ? e2 + "周" : e2 + "日";
      }, weekStart: 1, yearStart: 4, formats: { LT: "HH:mm", LTS: "HH:mm:ss", L: "YYYY/MM/DD", LL: "YYYY年M月D日", LLL: "YYYY年M月D日Ah点mm分", LLLL: "YYYY年M月D日ddddAh点mm分", l: "YYYY/M/D", ll: "YYYY年M月D日", lll: "YYYY年M月D日 HH:mm", llll: "YYYY年M月D日dddd HH:mm" }, relativeTime: { future: "%s内", past: "%s前", s: "几秒", m: "1 分钟", mm: "%d 分钟", h: "1 小时", hh: "%d 小时", d: "1 天", dd: "%d 天", M: "1 个月", MM: "%d 个月", y: "1 年", yy: "%d 年" }, meridiem: function(e2, _2) {
        var t2 = 100 * e2 + _2;
        return t2 < 600 ? "凌晨" : t2 < 900 ? "早上" : t2 < 1100 ? "上午" : t2 < 1300 ? "中午" : t2 < 1800 ? "下午" : "晚上";
      } };
      return t.default.locale(d, null, true), d;
    }));
  }
});

// node_modules/tdesign-miniprogram/miniprogram_dist/qrcode/qrcode.js
var qrcode_exports = {};
__export(qrcode_exports, {
  default: () => qrcode_default
});
module.exports = __toCommonJS(qrcode_exports);

// node_modules/tslib/tslib.es6.mjs
var extendStatics = function(d, b) {
  extendStatics = Object.setPrototypeOf || { __proto__: [] } instanceof Array && function(d2, b2) {
    d2.__proto__ = b2;
  } || function(d2, b2) {
    for (var p in b2) if (Object.prototype.hasOwnProperty.call(b2, p)) d2[p] = b2[p];
  };
  return extendStatics(d, b);
};
function __extends(d, b) {
  if (typeof b !== "function" && b !== null)
    throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
  extendStatics(d, b);
  function __() {
    this.constructor = d;
  }
  d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
}
var __assign = function() {
  __assign = Object.assign || function __assign2(t) {
    for (var s, i = 1, n = arguments.length; i < n; i++) {
      s = arguments[i];
      for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p)) t[p] = s[p];
    }
    return t;
  };
  return __assign.apply(this, arguments);
};
function __rest(s, e) {
  var t = {};
  for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p) && e.indexOf(p) < 0)
    t[p] = s[p];
  if (s != null && typeof Object.getOwnPropertySymbols === "function")
    for (var i = 0, p = Object.getOwnPropertySymbols(s); i < p.length; i++) {
      if (e.indexOf(p[i]) < 0 && Object.prototype.propertyIsEnumerable.call(s, p[i]))
        t[p[i]] = s[p[i]];
    }
  return t;
}
function __decorate(decorators, target, key, desc) {
  var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
  if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
  else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
  return c > 3 && r && Object.defineProperty(target, key, r), r;
}
function __param(paramIndex, decorator) {
  return function(target, key) {
    decorator(target, key, paramIndex);
  };
}
function __esDecorate(ctor, descriptorIn, decorators, contextIn, initializers, extraInitializers) {
  function accept(f) {
    if (f !== void 0 && typeof f !== "function") throw new TypeError("Function expected");
    return f;
  }
  var kind = contextIn.kind, key = kind === "getter" ? "get" : kind === "setter" ? "set" : "value";
  var target = !descriptorIn && ctor ? contextIn["static"] ? ctor : ctor.prototype : null;
  var descriptor = descriptorIn || (target ? Object.getOwnPropertyDescriptor(target, contextIn.name) : {});
  var _, done = false;
  for (var i = decorators.length - 1; i >= 0; i--) {
    var context = {};
    for (var p in contextIn) context[p] = p === "access" ? {} : contextIn[p];
    for (var p in contextIn.access) context.access[p] = contextIn.access[p];
    context.addInitializer = function(f) {
      if (done) throw new TypeError("Cannot add initializers after decoration has completed");
      extraInitializers.push(accept(f || null));
    };
    var result = (0, decorators[i])(kind === "accessor" ? { get: descriptor.get, set: descriptor.set } : descriptor[key], context);
    if (kind === "accessor") {
      if (result === void 0) continue;
      if (result === null || typeof result !== "object") throw new TypeError("Object expected");
      if (_ = accept(result.get)) descriptor.get = _;
      if (_ = accept(result.set)) descriptor.set = _;
      if (_ = accept(result.init)) initializers.unshift(_);
    } else if (_ = accept(result)) {
      if (kind === "field") initializers.unshift(_);
      else descriptor[key] = _;
    }
  }
  if (target) Object.defineProperty(target, contextIn.name, descriptor);
  done = true;
}
;
function __runInitializers(thisArg, initializers, value) {
  var useValue = arguments.length > 2;
  for (var i = 0; i < initializers.length; i++) {
    value = useValue ? initializers[i].call(thisArg, value) : initializers[i].call(thisArg);
  }
  return useValue ? value : void 0;
}
;
function __propKey(x) {
  return typeof x === "symbol" ? x : "".concat(x);
}
;
function __setFunctionName(f, name, prefix3) {
  if (typeof name === "symbol") name = name.description ? "[".concat(name.description, "]") : "";
  return Object.defineProperty(f, "name", { configurable: true, value: prefix3 ? "".concat(prefix3, " ", name) : name });
}
;
function __metadata(metadataKey, metadataValue) {
  if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(metadataKey, metadataValue);
}
function __awaiter(thisArg, _arguments, P, generator) {
  function adopt(value) {
    return value instanceof P ? value : new P(function(resolve) {
      resolve(value);
    });
  }
  return new (P || (P = Promise))(function(resolve, reject) {
    function fulfilled(value) {
      try {
        step(generator.next(value));
      } catch (e) {
        reject(e);
      }
    }
    function rejected(value) {
      try {
        step(generator["throw"](value));
      } catch (e) {
        reject(e);
      }
    }
    function step(result) {
      result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected);
    }
    step((generator = generator.apply(thisArg, _arguments || [])).next());
  });
}
function __generator(thisArg, body) {
  var _ = { label: 0, sent: function() {
    if (t[0] & 1) throw t[1];
    return t[1];
  }, trys: [], ops: [] }, f, y, t, g = Object.create((typeof Iterator === "function" ? Iterator : Object).prototype);
  return g.next = verb(0), g["throw"] = verb(1), g["return"] = verb(2), typeof Symbol === "function" && (g[Symbol.iterator] = function() {
    return this;
  }), g;
  function verb(n) {
    return function(v) {
      return step([n, v]);
    };
  }
  function step(op) {
    if (f) throw new TypeError("Generator is already executing.");
    while (g && (g = 0, op[0] && (_ = 0)), _) try {
      if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
      if (y = 0, t) op = [op[0] & 2, t.value];
      switch (op[0]) {
        case 0:
        case 1:
          t = op;
          break;
        case 4:
          _.label++;
          return { value: op[1], done: false };
        case 5:
          _.label++;
          y = op[1];
          op = [0];
          continue;
        case 7:
          op = _.ops.pop();
          _.trys.pop();
          continue;
        default:
          if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) {
            _ = 0;
            continue;
          }
          if (op[0] === 3 && (!t || op[1] > t[0] && op[1] < t[3])) {
            _.label = op[1];
            break;
          }
          if (op[0] === 6 && _.label < t[1]) {
            _.label = t[1];
            t = op;
            break;
          }
          if (t && _.label < t[2]) {
            _.label = t[2];
            _.ops.push(op);
            break;
          }
          if (t[2]) _.ops.pop();
          _.trys.pop();
          continue;
      }
      op = body.call(thisArg, _);
    } catch (e) {
      op = [6, e];
      y = 0;
    } finally {
      f = t = 0;
    }
    if (op[0] & 5) throw op[1];
    return { value: op[0] ? op[1] : void 0, done: true };
  }
}
var __createBinding = Object.create ? (function(o, m, k, k2) {
  if (k2 === void 0) k2 = k;
  var desc = Object.getOwnPropertyDescriptor(m, k);
  if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
    desc = { enumerable: true, get: function() {
      return m[k];
    } };
  }
  Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
  if (k2 === void 0) k2 = k;
  o[k2] = m[k];
});
function __exportStar(m, o) {
  for (var p in m) if (p !== "default" && !Object.prototype.hasOwnProperty.call(o, p)) __createBinding(o, m, p);
}
function __values(o) {
  var s = typeof Symbol === "function" && Symbol.iterator, m = s && o[s], i = 0;
  if (m) return m.call(o);
  if (o && typeof o.length === "number") return {
    next: function() {
      if (o && i >= o.length) o = void 0;
      return { value: o && o[i++], done: !o };
    }
  };
  throw new TypeError(s ? "Object is not iterable." : "Symbol.iterator is not defined.");
}
function __read(o, n) {
  var m = typeof Symbol === "function" && o[Symbol.iterator];
  if (!m) return o;
  var i = m.call(o), r, ar = [], e;
  try {
    while ((n === void 0 || n-- > 0) && !(r = i.next()).done) ar.push(r.value);
  } catch (error) {
    e = { error };
  } finally {
    try {
      if (r && !r.done && (m = i["return"])) m.call(i);
    } finally {
      if (e) throw e.error;
    }
  }
  return ar;
}
function __spread() {
  for (var ar = [], i = 0; i < arguments.length; i++)
    ar = ar.concat(__read(arguments[i]));
  return ar;
}
function __spreadArrays() {
  for (var s = 0, i = 0, il = arguments.length; i < il; i++) s += arguments[i].length;
  for (var r = Array(s), k = 0, i = 0; i < il; i++)
    for (var a = arguments[i], j = 0, jl = a.length; j < jl; j++, k++)
      r[k] = a[j];
  return r;
}
function __spreadArray(to, from, pack) {
  if (pack || arguments.length === 2) for (var i = 0, l = from.length, ar; i < l; i++) {
    if (ar || !(i in from)) {
      if (!ar) ar = Array.prototype.slice.call(from, 0, i);
      ar[i] = from[i];
    }
  }
  return to.concat(ar || Array.prototype.slice.call(from));
}
function __await(v) {
  return this instanceof __await ? (this.v = v, this) : new __await(v);
}
function __asyncGenerator(thisArg, _arguments, generator) {
  if (!Symbol.asyncIterator) throw new TypeError("Symbol.asyncIterator is not defined.");
  var g = generator.apply(thisArg, _arguments || []), i, q = [];
  return i = Object.create((typeof AsyncIterator === "function" ? AsyncIterator : Object).prototype), verb("next"), verb("throw"), verb("return", awaitReturn), i[Symbol.asyncIterator] = function() {
    return this;
  }, i;
  function awaitReturn(f) {
    return function(v) {
      return Promise.resolve(v).then(f, reject);
    };
  }
  function verb(n, f) {
    if (g[n]) {
      i[n] = function(v) {
        return new Promise(function(a, b) {
          q.push([n, v, a, b]) > 1 || resume(n, v);
        });
      };
      if (f) i[n] = f(i[n]);
    }
  }
  function resume(n, v) {
    try {
      step(g[n](v));
    } catch (e) {
      settle(q[0][3], e);
    }
  }
  function step(r) {
    r.value instanceof __await ? Promise.resolve(r.value.v).then(fulfill, reject) : settle(q[0][2], r);
  }
  function fulfill(value) {
    resume("next", value);
  }
  function reject(value) {
    resume("throw", value);
  }
  function settle(f, v) {
    if (f(v), q.shift(), q.length) resume(q[0][0], q[0][1]);
  }
}
function __asyncDelegator(o) {
  var i, p;
  return i = {}, verb("next"), verb("throw", function(e) {
    throw e;
  }), verb("return"), i[Symbol.iterator] = function() {
    return this;
  }, i;
  function verb(n, f) {
    i[n] = o[n] ? function(v) {
      return (p = !p) ? { value: __await(o[n](v)), done: false } : f ? f(v) : v;
    } : f;
  }
}
function __asyncValues(o) {
  if (!Symbol.asyncIterator) throw new TypeError("Symbol.asyncIterator is not defined.");
  var m = o[Symbol.asyncIterator], i;
  return m ? m.call(o) : (o = typeof __values === "function" ? __values(o) : o[Symbol.iterator](), i = {}, verb("next"), verb("throw"), verb("return"), i[Symbol.asyncIterator] = function() {
    return this;
  }, i);
  function verb(n) {
    i[n] = o[n] && function(v) {
      return new Promise(function(resolve, reject) {
        v = o[n](v), settle(resolve, reject, v.done, v.value);
      });
    };
  }
  function settle(resolve, reject, d, v) {
    Promise.resolve(v).then(function(v2) {
      resolve({ value: v2, done: d });
    }, reject);
  }
}
function __makeTemplateObject(cooked, raw) {
  if (Object.defineProperty) {
    Object.defineProperty(cooked, "raw", { value: raw });
  } else {
    cooked.raw = raw;
  }
  return cooked;
}
;
var __setModuleDefault = Object.create ? (function(o, v) {
  Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
  o["default"] = v;
};
var ownKeys = function(o) {
  ownKeys = Object.getOwnPropertyNames || function(o2) {
    var ar = [];
    for (var k in o2) if (Object.prototype.hasOwnProperty.call(o2, k)) ar[ar.length] = k;
    return ar;
  };
  return ownKeys(o);
};
function __importStar(mod) {
  if (mod && mod.__esModule) return mod;
  var result = {};
  if (mod != null) {
    for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
  }
  __setModuleDefault(result, mod);
  return result;
}
function __importDefault(mod) {
  return mod && mod.__esModule ? mod : { default: mod };
}
function __classPrivateFieldGet(receiver, state, kind, f) {
  if (kind === "a" && !f) throw new TypeError("Private accessor was defined without a getter");
  if (typeof state === "function" ? receiver !== state || !f : !state.has(receiver)) throw new TypeError("Cannot read private member from an object whose class did not declare it");
  return kind === "m" ? f : kind === "a" ? f.call(receiver) : f ? f.value : state.get(receiver);
}
function __classPrivateFieldSet(receiver, state, value, kind, f) {
  if (kind === "m") throw new TypeError("Private method is not writable");
  if (kind === "a" && !f) throw new TypeError("Private accessor was defined without a setter");
  if (typeof state === "function" ? receiver !== state || !f : !state.has(receiver)) throw new TypeError("Cannot write private member to an object whose class did not declare it");
  return kind === "a" ? f.call(receiver, value) : f ? f.value = value : state.set(receiver, value), value;
}
function __classPrivateFieldIn(state, receiver) {
  if (receiver === null || typeof receiver !== "object" && typeof receiver !== "function") throw new TypeError("Cannot use 'in' operator on non-object");
  return typeof state === "function" ? receiver === state : state.has(receiver);
}
function __addDisposableResource(env, value, async) {
  if (value !== null && value !== void 0) {
    if (typeof value !== "object" && typeof value !== "function") throw new TypeError("Object expected.");
    var dispose, inner;
    if (async) {
      if (!Symbol.asyncDispose) throw new TypeError("Symbol.asyncDispose is not defined.");
      dispose = value[Symbol.asyncDispose];
    }
    if (dispose === void 0) {
      if (!Symbol.dispose) throw new TypeError("Symbol.dispose is not defined.");
      dispose = value[Symbol.dispose];
      if (async) inner = dispose;
    }
    if (typeof dispose !== "function") throw new TypeError("Object not disposable.");
    if (inner) dispose = function() {
      try {
        inner.call(this);
      } catch (e) {
        return Promise.reject(e);
      }
    };
    env.stack.push({ value, dispose, async });
  } else if (async) {
    env.stack.push({ async: true });
  }
  return value;
}
var _SuppressedError = typeof SuppressedError === "function" ? SuppressedError : function(error, suppressed, message) {
  var e = new Error(message);
  return e.name = "SuppressedError", e.error = error, e.suppressed = suppressed, e;
};
function __disposeResources(env) {
  function fail(e) {
    env.error = env.hasError ? new _SuppressedError(e, env.error, "An error was suppressed during disposal.") : e;
    env.hasError = true;
  }
  var r, s = 0;
  function next() {
    while (r = env.stack.pop()) {
      try {
        if (!r.async && s === 1) return s = 0, env.stack.push(r), Promise.resolve().then(next);
        if (r.dispose) {
          var result = r.dispose.call(r.value);
          if (r.async) return s |= 2, Promise.resolve(result).then(next, function(e) {
            fail(e);
            return next();
          });
        } else s |= 1;
      } catch (e) {
        fail(e);
      }
    }
    if (s === 1) return env.hasError ? Promise.reject(env.error) : Promise.resolve();
    if (env.hasError) throw env.error;
  }
  return next();
}
function __rewriteRelativeImportExtension(path, preserveJsx) {
  if (typeof path === "string" && /^\.\.?\//.test(path)) {
    return path.replace(/\.(tsx)$|((?:\.d)?)((?:\.[^./]+?)?)\.([cm]?)ts$/i, function(m, tsx, d, ext, cm) {
      return tsx ? preserveJsx ? ".jsx" : ".js" : d && (!ext || !cm) ? m : d + ext + "." + cm.toLowerCase() + "js";
    });
  }
  return path;
}
var tslib_es6_default = {
  __extends,
  __assign,
  __rest,
  __decorate,
  __param,
  __esDecorate,
  __runInitializers,
  __propKey,
  __setFunctionName,
  __metadata,
  __awaiter,
  __generator,
  __createBinding,
  __exportStar,
  __values,
  __read,
  __spread,
  __spreadArrays,
  __spreadArray,
  __await,
  __asyncGenerator,
  __asyncDelegator,
  __asyncValues,
  __makeTemplateObject,
  __importStar,
  __importDefault,
  __classPrivateFieldGet,
  __classPrivateFieldSet,
  __classPrivateFieldIn,
  __addDisposableResource,
  __disposeResources,
  __rewriteRelativeImportExtension
};

// node_modules/tdesign-miniprogram/miniprogram_dist/qrcode/props.js
var props = { bgColor: { type: String, value: "" }, borderless: { type: Boolean, value: false }, color: { type: String, value: "" }, icon: { type: String, value: "" }, iconSize: { type: null, value: 40 }, level: { type: String, value: "M" }, size: { type: Number, value: 160 }, status: { type: String, value: "active" }, value: { type: String, value: "" } };
var props_default = props;

// node_modules/tdesign-miniprogram/miniprogram_dist/common/config.js
var config_default = { prefix: "t" };
var prefix = "t";

// node_modules/tdesign-miniprogram/miniprogram_dist/common/src/superComponent.js
var SuperComponent = class {
  constructor() {
    this.app = getApp();
  }
};

// node_modules/tdesign-miniprogram/miniprogram_dist/common/validator.js
function isFunction(t) {
  return "function" == typeof t;
}
var isString = (t) => "string" == typeof t;
var isNull = (t) => null === t;
var isUndefined = (t) => void 0 === t;
function isDef(t) {
  return !isUndefined(t) && !isNull(t);
}
function isInteger(t) {
  return Number.isInteger(t);
}
function isNumeric(t) {
  return !Number.isNaN(Number(t));
}
function isNumber(t) {
  return "number" == typeof t;
}
function isBoolean(t) {
  return "boolean" == typeof t;
}
function isObject(t) {
  const e = typeof t;
  return null !== t && ("object" === e || "function" === e);
}
function isPlainObject(t) {
  return null !== t && "object" == typeof t && "[object Object]" === Object.prototype.toString.call(t);
}
function isEmpty(t) {
  return null == t || ("string" == typeof t || Array.isArray(t) ? 0 === t.length : t instanceof Map || t instanceof Set ? 0 === t.size : "object" != typeof t || 0 === Object.keys(t).length);
}
function isDate(t, e) {
  const r = Object.assign(Object.assign({}, { format: "YYYY/MM/DD", delimiters: ["/", "-"], strictMode: false }), e);
  if ("string" == typeof t) {
    const e2 = r.delimiters.find((t2) => r.format.includes(t2));
    if (!e2) return false;
    const n = r.format.split(e2), i = t.split(e2);
    if (n.length !== i.length) return false;
    let o = "", s = "", u = "";
    for (let t2 = 0; t2 < n.length; t2 += 1) {
      const e3 = n[t2].toUpperCase(), r2 = i[t2];
      e3.includes("Y") ? o = r2 : e3.includes("M") ? s = r2 : e3.includes("D") && (u = r2);
    }
    if (1 === s.length && (s = `0${s}`), 1 === u.length && (u = `0${u}`), 2 === o.length) {
      const t2 = (/* @__PURE__ */ new Date()).getFullYear() % 100;
      o = Number(o) <= t2 ? `20${o}` : `19${o}`;
    }
    const l = /* @__PURE__ */ new Date(`${o}-${s}-${u}T00:00:00.000Z`);
    return l.getUTCFullYear() === Number(o) && l.getUTCMonth() + 1 === Number(s) && l.getUTCDate() === Number(u);
  }
  return !(r.strictMode || "[object Date]" !== Object.prototype.toString.call(t) || !Number.isFinite(t.getTime()));
}
function isEmail(t) {
  if ("string" != typeof t) return false;
  if (t.length > 254) return false;
  const e = t.split("@");
  if (2 !== e.length) return false;
  const [r, n] = e;
  if (!r || r.length > 64) return false;
  if (!n) return false;
  if (/^[-.]/.test(n) || /[-.]$/.test(n)) return false;
  if (!/^[a-zA-Z0-9.-]+$/.test(n)) return false;
  if (!n.includes(".")) return false;
  const i = n.split(".").pop();
  if (!i || i.length < 2) return false;
  return /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+$/.test(r);
}
function isURL(t, e) {
  if ("string" != typeof t) return false;
  if (0 === t.length || /\s/.test(t)) return false;
  if (t.length > 2084) return false;
  const r = Object.assign(Object.assign({}, { protocols: ["http", "https", "ftp"], require_tld: true, require_protocol: false, require_host: true, allow_protocol_relative_urls: false }), e);
  let n = t;
  const i = n.match(/^([a-z][a-z0-9+\-.]*):\/\//i);
  if (i) {
    const t2 = i[1].toLowerCase();
    if (!r.protocols.includes(t2)) return false;
    n = n.slice(i[0].length);
  } else if (r.require_protocol) {
    if (!r.allow_protocol_relative_urls || !t.startsWith("//")) return false;
    n = n.slice(2);
  } else if (t.startsWith("//")) {
    if (!r.allow_protocol_relative_urls) return false;
    n = n.slice(2);
  }
  if (!n && r.require_host) return false;
  const [o] = n.split(/[/?#]/);
  if (!o && r.require_host) return false;
  let s = o;
  s.includes("@") && (s = s.split("@").pop() || "");
  let u = s;
  const l = s.match(/:(\d+)$/);
  if (l) {
    const t2 = Number(l[1]);
    if (t2 < 0 || t2 > 65535) return false;
    u = s.slice(0, s.lastIndexOf(":"));
  }
  if (!u) return false;
  const c = u.match(/^(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/);
  if (c) return c.slice(1).every((t2) => Number(t2) >= 0 && Number(t2) <= 255);
  if (u.startsWith("[") && u.endsWith("]")) return true;
  const f = u.split(".");
  if (r.require_tld && f.length < 2) return false;
  if (f.some((t2) => !t2 || t2.length > 63 || (!/^[a-zA-Z0-9-]+$/.test(t2) || !(!t2.startsWith("-") && !t2.endsWith("-"))))) return false;
  if (r.require_tld) {
    const t2 = f[f.length - 1];
    if (/^\d+$/.test(t2)) return false;
  }
  return true;
}

// node_modules/tdesign-miniprogram/miniprogram_dist/common/src/flatTool.js
var getPrototypeOf = function(t) {
  return Object.getPrototypeOf ? Object.getPrototypeOf(t) : t.__proto__;
};
var iterateInheritedPrototype = function(t, e, o, r = true) {
  let n = e.prototype || e;
  const c = o.prototype || o;
  for (; n && (r || n !== c) && false !== t(n) && n !== c; ) n = getPrototypeOf(n);
};
var toObject = function(t, e = {}) {
  const o = {};
  if (!isObject(t)) return o;
  const r = e.excludes || ["constructor"], { enumerable: n = true, configurable: c = 0, writable: i = 0 } = e, p = {};
  return 0 !== n && (p.enumerable = n), 0 !== c && (p.configurable = c), 0 !== i && (p.writable = i), iterateInheritedPrototype((t2) => {
    Object.getOwnPropertyNames(t2).forEach((n2) => {
      if (r.indexOf(n2) >= 0) return;
      if (Object.prototype.hasOwnProperty.call(o, n2)) return;
      const c2 = Object.getOwnPropertyDescriptor(t2, n2);
      ["get", "set", "value"].forEach((t3) => {
        if ("function" == typeof c2[t3]) {
          const o2 = c2[t3];
          c2[t3] = function(...t4) {
            return o2.apply(Object.prototype.hasOwnProperty.call(e, "bindTo") ? e.bindTo : this, t4);
          };
        }
      }), Object.defineProperty(o, n2, Object.assign(Object.assign({}, c2), p));
    });
  }, t, e.till || Object, false), o;
};

// node_modules/tdesign-miniprogram/miniprogram_dist/common/wechat.js
var getObserver = (e, t) => new Promise((o) => {
  e.createIntersectionObserver({ nativeMode: true }).relativeToViewport().observe(t, (e2) => {
    o(e2);
  });
});
var getWindowInfo = () => wx.getWindowInfo && wx.getWindowInfo() || wx.getSystemInfoSync();
var getAppBaseInfo = () => wx.getAppBaseInfo && wx.getAppBaseInfo() || wx.getSystemInfoSync();
var getDeviceInfo = () => wx.getDeviceInfo && wx.getDeviceInfo() || wx.getSystemInfoSync();

// node_modules/tdesign-miniprogram/miniprogram_dist/common/version.js
var systemInfo;
function getSystemInfo() {
  return null == systemInfo && (systemInfo = getAppBaseInfo()), systemInfo;
}
function compareVersion(e, n) {
  e = e.split("."), n = n.split(".");
  const t = Math.max(e.length, n.length);
  for (; e.length < t; ) e.push("0");
  for (; n.length < t; ) n.push("0");
  for (let r = 0; r < t; r += 1) {
    const t2 = parseInt(e[r], 10), o = parseInt(n[r], 10);
    if (t2 > o) return 1;
    if (t2 < o) return -1;
  }
  return 0;
}
function judgeByVersion(e) {
  return compareVersion(getSystemInfo().SDKVersion, e) >= 0;
}
function canIUseFormFieldButton() {
  return judgeByVersion("2.10.3");
}
function canUseVirtualHost() {
  return judgeByVersion("2.19.2");
}
function canUseProxyScrollView() {
  return judgeByVersion("2.19.2");
}

// node_modules/tdesign-miniprogram/miniprogram_dist/common/src/instantiationDecorator.js
var RawLifeCycles = ["Created", "Attached", "Ready", "Moved", "Detached", "Error"], NativeLifeCycles = RawLifeCycles.map((e) => e.toLowerCase()), ComponentNativeProps = ["properties", "data", "observers", "methods", "behaviors", ...NativeLifeCycles, "relations", "externalClasses", "options", "lifetimes", "pageLifeTimes", "definitionFilter"];
var toComponent = function(e) {
  const { relations: t, behaviors: o = [], externalClasses: i = [] } = e;
  if (e.properties) {
    Object.keys(e.properties).forEach((t2) => {
      let o2 = e.properties[t2];
      isPlainObject(o2) || (o2 = { type: o2 }), e.properties[t2] = o2;
    });
    [{ key: "ariaHidden", type: Boolean }, { key: "ariaRole", type: String }, { key: "ariaLabel", type: String }, { key: "ariaLabelledby", type: String }, { key: "ariaDescribedby", type: String }, { key: "ariaBusy", type: Boolean }].forEach(({ key: t2, type: o2 }) => {
      e.properties[t2] = { type: o2 };
    }), e.properties.style = { type: String, value: "" }, e.properties.customStyle = { type: String, value: "" };
  }
  e.methods || (e.methods = {}), e.lifetimes || (e.lifetimes = {});
  const s = {};
  if (t) {
    const e2 = (e3, t2) => Behavior({ created() {
      Object.defineProperty(this, `$${e3}`, { get: () => {
        const o2 = this.getRelationNodes(t2) || [];
        return "parent" === e3 ? o2[0] : o2;
      } });
    } }), i2 = {};
    Object.keys(t).forEach((o2) => {
      const s2 = t[o2], r = ["parent", "ancestor"].includes(s2.type) ? "parent" : "children", n = e2(r, o2);
      i2[r] = n;
    }), o.push(...Object.keys(i2).map((e3) => i2[e3]));
  }
  if (e.behaviors = [...o], e.externalClasses = ["class", ...i], Object.getOwnPropertyNames(e).forEach((t2) => {
    const o2 = Object.getOwnPropertyDescriptor(e, t2);
    o2 && (NativeLifeCycles.indexOf(t2) < 0 && "function" == typeof o2.value ? (Object.defineProperty(e.methods, t2, o2), delete e[t2]) : ComponentNativeProps.indexOf(t2) < 0 ? s[t2] = o2 : NativeLifeCycles.indexOf(t2) >= 0 && (e.lifetimes[t2] = e[t2]));
  }), Object.keys(s).length) {
    const t2 = e.lifetimes.created, o2 = e.lifetimes.attached, { controlledProps: i2 = [] } = e;
    e.lifetimes.created = function(...e2) {
      Object.defineProperties(this, s), t2 && t2.apply(this, e2);
    }, e.lifetimes.attached = function(...e2) {
      o2 && o2.apply(this, e2), i2.forEach(({ key: e3 }) => {
        const t3 = `default${e3.replace(/^(\w)/, (e4, t4) => t4.toUpperCase())}`, o3 = this.properties;
        null == o3[e3] && (this._selfControlled = true), null == o3[e3] && null != o3[t3] && this.setData({ [e3]: o3[t3] });
      });
    }, e.methods._trigger = function(e2, t3, o3) {
      const s2 = i2.find((t4) => t4.event === e2);
      if (s2) {
        const { key: e3 } = s2;
        this._selfControlled && this.setData({ [e3]: t3[e3] });
      }
      this.triggerEvent(e2, t3, o3);
    };
  }
  return e;
};
var wxComponent = function() {
  return function(e) {
    const t = new class extends e {
    }();
    t.options = t.options || {}, canUseVirtualHost() && (t.options.virtualHost = true);
    const o = toComponent(toObject(t));
    Component(o);
  };
};

// node_modules/tdesign-miniprogram/miniprogram_dist/config-provider/reactive-state.js
var ReactiveState = class {
  constructor(e) {
    this._listeners = /* @__PURE__ */ new Set(), this._value = e;
  }
  get value() {
    return this._value;
  }
  set value(e) {
    this._value !== e && (this._value = e, this._notify());
  }
  subscribe(e) {
    return this._listeners.add(e), e(this._value), () => {
      this._listeners.delete(e);
    };
  }
  _notify() {
    this._listeners.forEach((e) => {
      try {
        e(this._value);
      } catch (e2) {
        console.error("State listener error:", e2);
      }
    });
  }
};

// node_modules/tdesign-miniprogram/miniprogram_dist/config-provider/config-store.js
var ConfigStore = class {
  constructor() {
    this.currentLocale = new ReactiveState({}), this.themeVars = new ReactiveState({}), this._pageInitFlags = /* @__PURE__ */ new Map(), this._cleanupCallbacks = /* @__PURE__ */ new Map();
  }
  _deepEqual(e, t) {
    if (e === t) return true;
    if (typeof e != typeof t) return false;
    if (null == e || null == t) return e === t;
    if ("object" != typeof e) return false;
    const a = Object.keys(e), r = Object.keys(t);
    if (a.length !== r.length) return false;
    try {
      const a2 = JSON.stringify(e);
      if (a2 === JSON.stringify(t)) return true;
    } catch (e2) {
    }
    return a.every((a2) => this._deepEqual(e[a2], t[a2]));
  }
  switchLocale(e, t) {
    if (!t) return;
    const a = this._getOrInitPageFlag(t);
    if (a.locale) {
      (!e || 0 === Object.keys(e).length) === (0 === Object.keys(this.currentLocale.value).length) && this._deepEqual(e, this.currentLocale.value) || (this.currentLocale.value = e);
    } else a.locale = true, this.currentLocale.value = e;
  }
  updateThemeVars(e) {
    this.themeVars.value = Object.assign(Object.assign({}, this.themeVars.value), e);
  }
  _getOrInitPageFlag(e) {
    return this._pageInitFlags.has(e) || this._pageInitFlags.set(e, { theme: false, locale: false }), this._pageInitFlags.get(e);
  }
  registerCleanup(e, t) {
    this._cleanupCallbacks.set(e, t);
  }
  resetPageState(e) {
    if (e) {
      this._pageInitFlags.delete(e);
      const t = this._cleanupCallbacks.get(e);
      if (t) {
        try {
          t();
        } catch (t2) {
          console.error(`[ConfigStore] Error during cleanup for ${e}:`, t2);
        }
        this._cleanupCallbacks.delete(e);
      }
      Array.from(this._pageInitFlags.values()).some((e2) => e2.locale) || (this.currentLocale.value = {});
    }
  }
};
var configStore = new ConfigStore();

// node_modules/tdesign-miniprogram/miniprogram_dist/config-provider/use-config.js
function getComponentLocale(e, o, t, n) {
  var r;
  let c = {};
  n && (c = (null === (r = e.properties) || void 0 === r ? void 0 : r[n]) || {});
  const i = configStore.currentLocale.value, s = i && i[o] || {};
  return Object.assign(Object.assign(Object.assign({}, t), s), c);
}
function useConfig(e) {
  return { getLocale: (o, t) => getComponentLocale(t, e, o), subscribeLocale: (e2, o) => configStore.currentLocale.subscribe((e3) => {
    o(e3);
  }) };
}

// node_modules/tdesign-miniprogram/miniprogram_dist/common/utils.js
var systemInfo2 = getWindowInfo();
var appBaseInfo = getAppBaseInfo();
var deviceInfo = getDeviceInfo();
var debounce = function(e, t = 500) {
  let n;
  return function(...o) {
    n && clearTimeout(n), n = setTimeout(() => {
      e.apply(this, o);
    }, t);
  };
};
var throttle = (e, t = 100, n = null) => {
  let o = 0, r = null;
  return n || (n = { leading: true }), function(...c) {
    const s = Date.now();
    o || n.leading || (o = s);
    const i = this;
    t - (s - o) <= 0 && (r && (clearTimeout(r), r = null), o = s, e.apply(i, c));
  };
};
var classNames = function(...e) {
  const t = {}.hasOwnProperty, n = [];
  return e.forEach((e2) => {
    if (!e2) return;
    const o = typeof e2;
    if ("string" === o || "number" === o) n.push(e2);
    else if (Array.isArray(e2) && e2.length) {
      const t2 = classNames(...e2);
      t2 && n.push(t2);
    } else if ("object" === o) for (const o2 in e2) t.call(e2, o2) && e2[o2] && n.push(o2);
  }), n.join(" ");
};
var styles = function(e) {
  return Object.keys(e).map((t) => `${t}: ${e[t]}`).join("; ");
};
var getAnimationFrame = function(e, t) {
  return e.createSelectorQuery().selectViewport().boundingClientRect().exec(() => {
    t();
  });
};
var getRect = function(e, t, n = false) {
  return new Promise((o, r) => {
    e.createSelectorQuery()[n ? "selectAll" : "select"](t).boundingClientRect((e2) => {
      e2 ? o(e2) : r(e2);
    }).exec();
  });
};
var getTreeDepth = (e, t) => e.reduce((e2, n) => n[null != t ? t : "children"] && n[null != t ? t : "children"].length > 0 ? Math.max(e2, getTreeDepth(n[null != t ? t : "children"], t) + 1) : Math.max(e2, 1), 0);
var isIOS = function() {
  var e;
  return !!((null === (e = null == deviceInfo ? void 0 : deviceInfo.system) || void 0 === e ? void 0 : e.toLowerCase().search("ios")) + 1);
};
var isWxWork = "wxwork" === (null == deviceInfo ? void 0 : deviceInfo.environment);
var isPC = ["mac", "windows"].includes(null == deviceInfo ? void 0 : deviceInfo.platform);
var addUnit = function(e) {
  if (isDef(e)) return e = String(e), isNumeric(e) ? `${e}px` : e;
};
var getCharacterLength = (e, t, n) => {
  const o = String(null != t ? t : "");
  if (0 === o.length) return { length: 0, characters: "" };
  if ("maxcharacter" === e) {
    let e2 = 0;
    for (let t2 = 0; t2 < o.length; t2 += 1) {
      let r = 0;
      if (r = o.charCodeAt(t2) > 127 || 94 === o.charCodeAt(t2) ? 2 : 1, e2 + r > n) return { length: e2, characters: o.slice(0, t2) };
      e2 += r;
    }
    return { length: e2, characters: o };
  }
  if ("maxlength" === e) {
    const e2 = o.length > n ? n : o.length;
    return { length: e2, characters: o.slice(0, e2) };
  }
  return { length: o.length, characters: o };
};
var chunk = (e, t) => Array.from({ length: Math.ceil(e.length / t) }, (n, o) => e.slice(o * t, o * t + t));
var getInstance = function(e, t) {
  if (!e) {
    const t2 = getCurrentPages(), n2 = t2[t2.length - 1];
    e = n2.$$basePage || n2;
  }
  const n = e ? e.selectComponent(t) : null;
  return n || (console.warn("未找到组件,请检查selector是否正确"), null);
};
var unitConvert = (e) => {
  var t;
  return "string" == typeof e ? e.includes("rpx") ? parseInt(e, 10) * (null !== (t = null == systemInfo2 ? void 0 : systemInfo2.screenWidth) && void 0 !== t ? t : 750) / 750 : parseInt(e, 10) : null != e ? e : 0;
};
var setIcon = (e, t, n) => t ? "string" == typeof t ? { [`${e}Name`]: t, [`${e}Data`]: {} } : "object" == typeof t ? { [`${e}Name`]: "", [`${e}Data`]: t } : { [`${e}Name`]: n, [`${e}Data`]: {} } : { [`${e}Name`]: "", [`${e}Data`]: {} };
var toCamel = (e) => e.replace(/-(\w)/g, (e2, t) => t.toUpperCase());
function toKebabCase(e) {
  return e.replace(/([a-z])([A-Z])/g, "$1-$2").replace(/([A-Z])([A-Z][a-z])/g, "$1-$2").replace(/([0-9])([a-zA-Z])/g, "$1-$2").toLowerCase();
}
var getCurrentPage = function() {
  const e = getCurrentPages();
  return e[e.length - 1];
};
var uniqueFactory = (e) => {
  let t = 0;
  return () => {
    const n = `${prefix}_${e}_${t}`;
    return t += 1, n;
  };
};
var calcIcon = (e, t) => e && (isBoolean(e) && t || isString(e)) ? { name: isBoolean(e) ? t : e } : isObject(e) ? e : null;
var isOverSize = (e, t) => {
  var n;
  if (!t) return false;
  const o = 1e3, r = { B: 1, KB: o, MB: 1e6, GB: 1e9 };
  return e > ("number" == typeof t ? t * o : (null == t ? void 0 : t.size) * r[null !== (n = null == t ? void 0 : t.unit) && void 0 !== n ? n : "KB"]);
};
var rpx2px = (e) => Math.floor(systemInfo2.windowWidth * e / 750);
var nextTick = () => new Promise((e) => {
  wx.nextTick(() => {
    e();
  });
});

// node_modules/tdesign-miniprogram/miniprogram_dist/locale/zh_CN.js
var import_zh_cn = __toESM(require_zh_cn());
var zh_CN_default = { actionSheet: { cancel: "取消" }, calendar: { title: "请选择日期", confirm: "确认", weekdays: ["日", "一", "二", "三", "四", "五", "六"], monthTitle: "{year} 年 {month}", months: ["1 月", "2 月", "3 月", "4 月", "5 月", "6 月", "7 月", "8 月", "9 月", "10 月", "11 月", "12 月"] }, cascader: { title: "标题", placeholder: "选择选项" }, dropdownMenu: { reset: "重置", confirm: "确定" }, dateTimePicker: { dayjsLocale: "zh-cn", title: "选择时间", cancel: "取消", confirm: "确定", format: "YYYY-MM-DD", months: ["1 月", "2 月", "3 月", "4 月", "5 月", "6 月", "7 月", "8 月", "9 月", "10 月", "11 月", "12 月"], yearLabel: "年", monthLabel: "月", dateLabel: "日", hourLabel: "时", minuteLabel: "分", secondLabel: "秒" }, form: { errorMessage: { date: "请输入正确的${name}", url: "请输入正确的${name}", required: "${name}必填", whitespace: "${name}不能为空", max: "${name}字符长度不能超过 ${validate} 个字符，一个中文等于两个字符", min: "${name}字符长度不能少于 ${validate} 个字符，一个中文等于两个字符", len: "${name}字符长度必须是 ${validate}", enum: "${name}只能是${validate}等", idcard: "请输入正确的${name}", telnumber: "请输入正确的${name}", pattern: "请输入正确的${name}", validator: "${name}不符合要求", boolean: "${name}数据类型必须是布尔类型", number: "${name}必须是数字" }, colonText: "：" }, picker: { cancel: "取消", confirm: "确认" }, pullDownRefresh: { loadingTexts: ["下拉刷新", "松手刷新", "正在刷新", "刷新完成"] }, rate: { texts: ["极差", "失望", "一般", "满意", "惊喜"], valueText: "{value} 分", noValueText: "未评分" }, tabBar: { newsAriaLabel: "有新的消息", moreNewsAriaLabel: "有很多消息", haveMoreNewsAriaLabel: "有 {value}+ 条消息", haveNewsAriaLabel: "有 {value} 条消息" }, table: { empty: "暂无数据" }, list: { loading: "加载中...", loadingMoreText: "点击加载更多", pulling: "下拉即可刷新...", loosing: "释放即可刷新...", success: "刷新成功" }, upload: { progress: { uploadingText: "上传中...", waitingText: "待上传", failText: "上传失败", successText: "上传成功", reloadText: "重新上传" } }, guide: { next: "下一步", skip: "跳过", finish: "完成", back: "返回" }, qrcode: { expiredText: "二维码过期", refreshText: "点击刷新", scannedText: "已扫描" }, attachments: { status: { pending: "上传中...", fail: "上传失败" } }, chatActionbar: { actionBar: { replay: "刷新", copy: "复制", good: "点赞", bad: "点踩", share: "分享", quote: "引用" } }, chatSender: { placeholder: "请输入消息...", sendText: "发送", stopText: "停止" }, chatThinking: { status: { pending: "正在思考中...", complete: "已完成思考", stop: "已停止思考" } } };

// node_modules/tdesign-miniprogram/miniprogram_dist/mixins/using-config.js
function usingConfig(o) {
  const { componentName: e, localeTextPropName: t } = o, a = toCamel(e);
  return Behavior({ data: { globalConfig: {} }, lifetimes: { attached() {
    var o2;
    null === (o2 = this.updateLocale) || void 0 === o2 || o2.call(this);
    const e2 = useConfig(a);
    this._unsubscribeLocale = e2.subscribeLocale(this, () => {
      var o3;
      null === (o3 = this.updateLocale) || void 0 === o3 || o3.call(this);
    });
  }, detached() {
    const o2 = this._unsubscribeLocale;
    o2 && (o2(), this._unsubscribeLocale = null);
  } }, methods: { updateLocale() {
    const o2 = zh_CN_default[a] || {}, e2 = getComponentLocale(this, a, o2, t);
    this.setData({ globalConfig: e2 });
  } } });
}

// node_modules/tdesign-miniprogram/miniprogram_dist/qrcode/qrcode.js
var { prefix: prefix2 } = config_default, componentName = "qrcode";
var QRCode = class extends SuperComponent {
  constructor() {
    super(...arguments), this.behaviors = [usingConfig({ componentName: "qrcode" })], this.externalClasses = [`${prefix2}-class`, `${prefix2}-class-canvas`], this.options = { multipleSlots: true }, this.properties = Object.assign(Object.assign({}, props_default), { statusRender: { type: Boolean, value: false } }), this.data = { prefix: prefix2, showMask: false, classPrefix: `${prefix2}-qrcode`, canvasReady: false }, this.lifetimes = { ready() {
      return __awaiter(this, void 0, void 0, function* () {
        const e = this.selectComponent("#qrcodeCanvas"), s = yield e.getCanvasNode();
        this.setData({ canvasNode: s });
      });
    }, attached() {
      this.setData({ showMask: "active" !== this.properties.status });
    } }, this.observers = { status: function(e) {
      this.setData({ showMask: "active" !== e });
    } }, this.methods = { init() {
      const e = this.selectComponent("#qrcodeCanvas");
      e && e.initCanvas();
    }, handleDrawCompleted() {
      this.setData({ canvasReady: true });
    }, handleDrawError(e) {
      console.error("二维码绘制失败", e);
    }, handleRefresh() {
      this.triggerEvent("refresh");
    }, handleDownload() {
      return __awaiter(this, void 0, void 0, function* () {
        this.data.canvasNode ? wx.canvasToTempFilePath({ canvas: this.data.canvasNode, success: (e) => {
          wx.saveImageToPhotosAlbum({ filePath: e.tempFilePath });
        }, fail: (e) => {
          console.error("canvasToTempFilePath failed", e);
        } }, this) : console.error("未找到 canvas 节点");
      });
    } };
  }
};
QRCode = __decorate([wxComponent()], QRCode);
var qrcode_default = QRCode;
