var __defProp = Object.defineProperty;
var __getOwnPropDesc = Object.getOwnPropertyDescriptor;
var __getOwnPropNames = Object.getOwnPropertyNames;
var __hasOwnProp = Object.prototype.hasOwnProperty;
var __export = (target, all) => {
  for (var name2 in all)
    __defProp(target, name2, { get: all[name2], enumerable: true });
};
var __copyProps = (to, from, except, desc) => {
  if (from && typeof from === "object" || typeof from === "function") {
    for (let key of __getOwnPropNames(from))
      if (!__hasOwnProp.call(to, key) && key !== except)
        __defProp(to, key, { get: () => from[key], enumerable: !(desc = __getOwnPropDesc(from, key)) || desc.enumerable });
  }
  return to;
};
var __toCommonJS = (mod) => __copyProps(__defProp({}, "__esModule", { value: true }), mod);

// node_modules/tdesign-miniprogram/miniprogram_dist/loading/loading.js
var loading_exports = {};
__export(loading_exports, {
  default: () => loading_default
});
module.exports = __toCommonJS(loading_exports);

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
function __setFunctionName(f, name2, prefix3) {
  if (typeof name2 === "symbol") name2 = name2.description ? "[".concat(name2.description, "]") : "";
  return Object.defineProperty(f, "name", { configurable: true, value: prefix3 ? "".concat(prefix3, " ", name2) : name2 });
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

// node_modules/tdesign-miniprogram/miniprogram_dist/common/config.js
var config_default = { prefix: "t" };
var prefix = "t";

// node_modules/tdesign-miniprogram/miniprogram_dist/loading/props.js
var props = { delay: { type: Number, value: 0 }, duration: { type: Number, value: 800 }, fullscreen: { type: Boolean, value: false }, indicator: { type: Boolean, value: true }, inheritColor: { type: Boolean, value: false }, layout: { type: String, value: "horizontal" }, loading: { type: Boolean, value: true }, pause: { type: Boolean, value: false }, progress: { type: Number }, reverse: { type: Boolean }, size: { type: String, value: "20px" }, text: { type: String }, theme: { type: String, value: "circular" } };
var props_default = props;

// node_modules/tdesign-miniprogram/miniprogram_dist/loading/loading.js
var { prefix: prefix2 } = config_default, name = `${prefix2}-loading`;
var Loading = class extends SuperComponent {
  constructor() {
    super(...arguments), this.externalClasses = [`${prefix2}-class`, `${prefix2}-class-text`, `${prefix2}-class-indicator`], this.data = { prefix: prefix2, classPrefix: name, show: true }, this.options = { multipleSlots: true }, this.properties = Object.assign({}, props_default), this.timer = null, this.observers = { loading(e) {
      const { delay: t } = this.properties;
      this.timer && clearTimeout(this.timer), e && t ? this.timer = setTimeout(() => {
        this.setData({ show: e }), this.timer = null;
      }, t) : this.setData({ show: e });
    } }, this.lifetimes = { detached() {
      clearTimeout(this.timer);
    } };
  }
  refreshPage() {
    this.triggerEvent("reload");
  }
};
Loading = __decorate([wxComponent()], Loading);
var loading_default = Loading;
