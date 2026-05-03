var __defProp = Object.defineProperty;
var __getOwnPropDesc = Object.getOwnPropertyDescriptor;
var __getOwnPropNames = Object.getOwnPropertyNames;
var __hasOwnProp = Object.prototype.hasOwnProperty;
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
var __toCommonJS = (mod) => __copyProps(__defProp({}, "__esModule", { value: true }), mod);

// node_modules/tdesign-miniprogram/miniprogram_dist/qrcode/components/qrcode-canvas/qrcode-canvas.js
var qrcode_canvas_exports = {};
__export(qrcode_canvas_exports, {
  default: () => qrcode_canvas_default
});
module.exports = __toCommonJS(qrcode_canvas_exports);

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
function __setFunctionName(f, name, prefix) {
  if (typeof name === "symbol") name = name.description ? "[".concat(name.description, "]") : "";
  return Object.defineProperty(f, "name", { configurable: true, value: prefix ? "".concat(prefix, " ", name) : name });
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

// node_modules/tdesign-miniprogram/miniprogram_dist/common/shared/qrcode/qrcodegen.js
function appendBits(t, e, r) {
  if (e < 0 || e > 31 || t >>> e !== 0) throw new RangeError("Value out of range");
  for (let o = e - 1; o >= 0; o--) r.push(t >>> o & 1);
}
function getBit(t, e) {
  return !!(t >>> e & 1);
}
function assert(t) {
  if (!t) throw new Error("Assertion error");
}
var Mode = class {
  constructor(t, e) {
    this.modeBits = t, this.numBitsCharCount = e;
  }
  numCharCountBits(t) {
    return this.numBitsCharCount[Math.floor((t + 7) / 17)];
  }
};
Mode.NUMERIC = new Mode(1, [10, 12, 14]), Mode.ALPHANUMERIC = new Mode(2, [9, 11, 13]), Mode.BYTE = new Mode(4, [8, 16, 16]), Mode.KANJI = new Mode(8, [8, 10, 12]), Mode.ECI = new Mode(7, [0, 0, 0]);
var Ecc = class {
  constructor(t, e) {
    this.ordinal = t, this.formatBits = e;
  }
};
Ecc.LOW = new Ecc(0, 1), Ecc.MEDIUM = new Ecc(1, 0), Ecc.QUARTILE = new Ecc(2, 3), Ecc.HIGH = new Ecc(3, 2);
var QrSegment = class _QrSegment {
  constructor(t, e, r) {
    if (this.mode = t, this.numChars = e, this.bitData = r, e < 0) throw new RangeError("Invalid argument");
    this.bitData = r.slice();
  }
  static makeBytes(t) {
    const e = [];
    for (const r of t) appendBits(r, 8, e);
    return new _QrSegment(Mode.BYTE, t.length, e);
  }
  static makeNumeric(t) {
    if (!_QrSegment.isNumeric(t)) throw new RangeError("String contains non-numeric characters");
    const e = [];
    for (let r = 0; r < t.length; ) {
      const o = Math.min(t.length - r, 3);
      appendBits(parseInt(t.substring(r, r + o), 10), 3 * o + 1, e), r += o;
    }
    return new _QrSegment(Mode.NUMERIC, t.length, e);
  }
  static makeAlphanumeric(t) {
    if (!_QrSegment.isAlphanumeric(t)) throw new RangeError("String contains unencodable characters in alphanumeric mode");
    const e = [];
    let r;
    for (r = 0; r + 2 <= t.length; r += 2) {
      let o = 45 * _QrSegment.ALPHANUMERIC_CHARSET.indexOf(t.charAt(r));
      o += _QrSegment.ALPHANUMERIC_CHARSET.indexOf(t.charAt(r + 1)), appendBits(o, 11, e);
    }
    return r < t.length && appendBits(_QrSegment.ALPHANUMERIC_CHARSET.indexOf(t.charAt(r)), 6, e), new _QrSegment(Mode.ALPHANUMERIC, t.length, e);
  }
  static makeSegments(t) {
    return "" === t ? [] : _QrSegment.isNumeric(t) ? [_QrSegment.makeNumeric(t)] : _QrSegment.isAlphanumeric(t) ? [_QrSegment.makeAlphanumeric(t)] : [_QrSegment.makeBytes(_QrSegment.toUtf8ByteArray(t))];
  }
  static makeEci(t) {
    const e = [];
    if (t < 0) throw new RangeError("ECI assignment value out of range");
    if (t < 128) appendBits(t, 8, e);
    else if (t < 16384) appendBits(2, 2, e), appendBits(t, 14, e);
    else {
      if (!(t < 1e6)) throw new RangeError("ECI assignment value out of range");
      appendBits(6, 3, e), appendBits(t, 21, e);
    }
    return new _QrSegment(Mode.ECI, 0, e);
  }
  static isNumeric(t) {
    return _QrSegment.NUMERIC_REGEX.test(t);
  }
  static isAlphanumeric(t) {
    return _QrSegment.ALPHANUMERIC_REGEX.test(t);
  }
  getData() {
    return this.bitData.slice();
  }
  static getTotalBits(t, e) {
    let r = 0;
    for (const o of t) {
      const t2 = o.mode.numCharCountBits(e);
      if (o.numChars >= 1 << t2) return 1 / 0;
      r += 4 + t2 + o.bitData.length;
    }
    return r;
  }
  static toUtf8ByteArray(t) {
    const e = encodeURI(t), r = [];
    for (let t2 = 0; t2 < e.length; t2++) "%" !== e.charAt(t2) ? r.push(e.charCodeAt(t2)) : (r.push(parseInt(e.substring(t2 + 1, t2 + 3), 16)), t2 += 2);
    return r;
  }
};
QrSegment.NUMERIC_REGEX = /^[0-9]*$/, QrSegment.ALPHANUMERIC_REGEX = /^[A-Z0-9 $%*+.\/:-]*$/, QrSegment.ALPHANUMERIC_CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ $%*+-./:";
var QrCode = class _QrCode {
  constructor(t, e, r, o) {
    this.modules = [], this.isFunction = [];
    let s = o;
    if (this.version = t, this.errorCorrectionLevel = e, t < _QrCode.MIN_VERSION || t > _QrCode.MAX_VERSION) throw new RangeError("Version value out of range");
    if (s < -1 || s > 7) throw new RangeError("Mask value out of range");
    this.size = 4 * t + 17;
    const n = [];
    for (let t2 = 0; t2 < this.size; t2++) n.push(false);
    for (let t2 = 0; t2 < this.size; t2++) this.modules.push(n.slice()), this.isFunction.push(n.slice());
    this.drawFunctionPatterns();
    const i = this.addEccAndInterleave(r);
    if (this.drawCodewords(i), -1 === s) {
      let t2 = 1e9;
      for (let e2 = 0; e2 < 8; e2++) {
        this.applyMask(e2), this.drawFormatBits(e2);
        const r2 = this.getPenaltyScore();
        r2 < t2 && (s = e2, t2 = r2), this.applyMask(e2);
      }
    }
    assert(s >= 0 && s <= 7), this.mask = s, this.applyMask(s), this.drawFormatBits(s), this.isFunction = [];
  }
  static encodeText(t, e) {
    const r = QrSegment.makeSegments(t);
    return _QrCode.encodeSegments(r, e);
  }
  static encodeBinary(t, e) {
    const r = QrSegment.makeBytes(t);
    return _QrCode.encodeSegments([r], e);
  }
  static encodeSegments(t, e, r = 1, o = 40, s = -1, n = true) {
    if (!(_QrCode.MIN_VERSION <= r && r <= o && o <= _QrCode.MAX_VERSION) || s < -1 || s > 7) throw new RangeError("Invalid value");
    let i, a;
    for (i = r; ; i++) {
      const r2 = 8 * _QrCode.getNumDataCodewords(i, e), s2 = QrSegment.getTotalBits(t, i);
      if (s2 <= r2) {
        a = s2;
        break;
      }
      if (i >= o) throw new RangeError("Data too long");
    }
    let h = e;
    for (const t2 of [Ecc.MEDIUM, Ecc.QUARTILE, Ecc.HIGH]) n && a <= 8 * _QrCode.getNumDataCodewords(i, t2) && (h = t2);
    const l = [];
    for (const e2 of t) {
      appendBits(e2.mode.modeBits, 4, l), appendBits(e2.numChars, e2.mode.numCharCountBits(i), l);
      for (const t2 of e2.getData()) l.push(t2);
    }
    assert(l.length === a);
    const d = 8 * _QrCode.getNumDataCodewords(i, h);
    assert(l.length <= d), appendBits(0, Math.min(4, d - l.length), l), appendBits(0, (8 - l.length % 8) % 8, l), assert(l.length % 8 == 0);
    for (let t2 = 236; l.length < d; t2 ^= 253) appendBits(t2, 8, l);
    const c = [];
    for (; 8 * c.length < l.length; ) c.push(0);
    return l.forEach((t2, e2) => {
      c[e2 >>> 3] |= t2 << 7 - (7 & e2);
    }), new _QrCode(i, h, c, s);
  }
  getModule(t, e) {
    return t >= 0 && t < this.size && e >= 0 && e < this.size && this.modules[e][t];
  }
  getModules() {
    return this.modules;
  }
  drawFunctionPatterns() {
    for (let t2 = 0; t2 < this.size; t2++) this.setFunctionModule(6, t2, t2 % 2 == 0), this.setFunctionModule(t2, 6, t2 % 2 == 0);
    this.drawFinderPattern(3, 3), this.drawFinderPattern(this.size - 4, 3), this.drawFinderPattern(3, this.size - 4);
    const t = this.getAlignmentPatternPositions(), e = t.length;
    for (let r = 0; r < e; r++) for (let o = 0; o < e; o++) 0 === r && 0 === o || 0 === r && o === e - 1 || r === e - 1 && 0 === o || this.drawAlignmentPattern(t[r], t[o]);
    this.drawFormatBits(0), this.drawVersion();
  }
  drawFormatBits(t) {
    const e = this.errorCorrectionLevel.formatBits << 3 | t;
    let r = e;
    for (let t2 = 0; t2 < 10; t2++) r = r << 1 ^ 1335 * (r >>> 9);
    const o = 21522 ^ (e << 10 | r);
    assert(o >>> 15 == 0);
    for (let t2 = 0; t2 <= 5; t2++) this.setFunctionModule(8, t2, getBit(o, t2));
    this.setFunctionModule(8, 7, getBit(o, 6)), this.setFunctionModule(8, 8, getBit(o, 7)), this.setFunctionModule(7, 8, getBit(o, 8));
    for (let t2 = 9; t2 < 15; t2++) this.setFunctionModule(14 - t2, 8, getBit(o, t2));
    for (let t2 = 0; t2 < 8; t2++) this.setFunctionModule(this.size - 1 - t2, 8, getBit(o, t2));
    for (let t2 = 8; t2 < 15; t2++) this.setFunctionModule(8, this.size - 15 + t2, getBit(o, t2));
    this.setFunctionModule(8, this.size - 8, true);
  }
  drawVersion() {
    if (this.version < 7) return;
    let t = this.version;
    for (let e2 = 0; e2 < 12; e2++) t = t << 1 ^ 7973 * (t >>> 11);
    const e = this.version << 12 | t;
    assert(e >>> 18 == 0);
    for (let t2 = 0; t2 < 18; t2++) {
      const r = getBit(e, t2), o = this.size - 11 + t2 % 3, s = Math.floor(t2 / 3);
      this.setFunctionModule(o, s, r), this.setFunctionModule(s, o, r);
    }
  }
  drawFinderPattern(t, e) {
    for (let r = -4; r <= 4; r++) for (let o = -4; o <= 4; o++) {
      const s = Math.max(Math.abs(o), Math.abs(r)), n = t + o, i = e + r;
      n >= 0 && n < this.size && i >= 0 && i < this.size && this.setFunctionModule(n, i, 2 !== s && 4 !== s);
    }
  }
  drawAlignmentPattern(t, e) {
    for (let r = -2; r <= 2; r++) for (let o = -2; o <= 2; o++) this.setFunctionModule(t + o, e + r, 1 !== Math.max(Math.abs(o), Math.abs(r)));
  }
  setFunctionModule(t, e, r) {
    this.modules[e][t] = r, this.isFunction[e][t] = true;
  }
  addEccAndInterleave(t) {
    const e = this.version, r = this.errorCorrectionLevel;
    if (t.length !== _QrCode.getNumDataCodewords(e, r)) throw new RangeError("Invalid argument");
    const o = _QrCode.NUM_ERROR_CORRECTION_BLOCKS[r.ordinal][e], s = _QrCode.ECC_CODEWORDS_PER_BLOCK[r.ordinal][e], n = Math.floor(_QrCode.getNumRawDataModules(e) / 8), i = o - n % o, a = Math.floor(n / o), h = [], l = _QrCode.reedSolomonComputeDivisor(s);
    for (let e2 = 0, r2 = 0; e2 < o; e2++) {
      const o2 = t.slice(r2, r2 + a - s + (e2 < i ? 0 : 1));
      r2 += o2.length;
      const n2 = _QrCode.reedSolomonComputeRemainder(o2, l);
      e2 < i && o2.push(0), h.push(o2.concat(n2));
    }
    const d = [];
    for (let t2 = 0; t2 < h[0].length; t2++) h.forEach((e2, r2) => {
      (t2 !== a - s || r2 >= i) && d.push(e2[t2]);
    });
    return assert(d.length === n), d;
  }
  drawCodewords(t) {
    if (t.length !== Math.floor(_QrCode.getNumRawDataModules(this.version) / 8)) throw new RangeError("Invalid argument");
    let e = 0;
    for (let r = this.size - 1; r >= 1; r -= 2) {
      6 === r && (r = 5);
      for (let o = 0; o < this.size; o++) for (let s = 0; s < 2; s++) {
        const n = r - s, i = !(r + 1 & 2) ? this.size - 1 - o : o;
        !this.isFunction[i][n] && e < 8 * t.length && (this.modules[i][n] = getBit(t[e >>> 3], 7 - (7 & e)), e++);
      }
    }
    assert(e === 8 * t.length);
  }
  applyMask(t) {
    if (t < 0 || t > 7) throw new RangeError("Mask value out of range");
    for (let e = 0; e < this.size; e++) for (let r = 0; r < this.size; r++) {
      let o;
      switch (t) {
        case 0:
          o = (r + e) % 2 == 0;
          break;
        case 1:
          o = e % 2 == 0;
          break;
        case 2:
          o = r % 3 == 0;
          break;
        case 3:
          o = (r + e) % 3 == 0;
          break;
        case 4:
          o = (Math.floor(r / 3) + Math.floor(e / 2)) % 2 == 0;
          break;
        case 5:
          o = r * e % 2 + r * e % 3 == 0;
          break;
        case 6:
          o = (r * e % 2 + r * e % 3) % 2 == 0;
          break;
        case 7:
          o = ((r + e) % 2 + r * e % 3) % 2 == 0;
          break;
        default:
          throw new Error("Unreachable");
      }
      !this.isFunction[e][r] && o && (this.modules[e][r] = !this.modules[e][r]);
    }
  }
  getPenaltyScore() {
    let t = 0;
    for (let e2 = 0; e2 < this.size; e2++) {
      let r2 = false, o2 = 0;
      const s = [0, 0, 0, 0, 0, 0, 0];
      for (let n = 0; n < this.size; n++) this.modules[e2][n] === r2 ? (o2++, 5 === o2 ? t += _QrCode.PENALTY_N1 : o2 > 5 && t++) : (this.finderPenaltyAddHistory(o2, s), r2 || (t += this.finderPenaltyCountPatterns(s) * _QrCode.PENALTY_N3), r2 = this.modules[e2][n], o2 = 1);
      t += this.finderPenaltyTerminateAndCount(r2, o2, s) * _QrCode.PENALTY_N3;
    }
    for (let e2 = 0; e2 < this.size; e2++) {
      let r2 = false, o2 = 0;
      const s = [0, 0, 0, 0, 0, 0, 0];
      for (let n = 0; n < this.size; n++) this.modules[n][e2] === r2 ? (o2++, 5 === o2 ? t += _QrCode.PENALTY_N1 : o2 > 5 && t++) : (this.finderPenaltyAddHistory(o2, s), r2 || (t += this.finderPenaltyCountPatterns(s) * _QrCode.PENALTY_N3), r2 = this.modules[n][e2], o2 = 1);
      t += this.finderPenaltyTerminateAndCount(r2, o2, s) * _QrCode.PENALTY_N3;
    }
    for (let e2 = 0; e2 < this.size - 1; e2++) for (let r2 = 0; r2 < this.size - 1; r2++) {
      const o2 = this.modules[e2][r2];
      o2 === this.modules[e2][r2 + 1] && o2 === this.modules[e2 + 1][r2] && o2 === this.modules[e2 + 1][r2 + 1] && (t += _QrCode.PENALTY_N2);
    }
    let e = 0;
    for (const t2 of this.modules) e = t2.reduce((t3, e2) => t3 + (e2 ? 1 : 0), e);
    const r = this.size * this.size, o = Math.ceil(Math.abs(20 * e - 10 * r) / r) - 1;
    return assert(o >= 0 && o <= 9), t += o * _QrCode.PENALTY_N4, assert(t >= 0 && t <= 2568888), t;
  }
  getAlignmentPatternPositions() {
    if (1 === this.version) return [];
    const t = Math.floor(this.version / 7) + 2, e = 32 === this.version ? 26 : 2 * Math.ceil((4 * this.version + 4) / (2 * t - 2)), r = [6];
    for (let o = this.size - 7; r.length < t; o -= e) r.splice(1, 0, o);
    return r;
  }
  static getNumRawDataModules(t) {
    if (t < _QrCode.MIN_VERSION || t > _QrCode.MAX_VERSION) throw new RangeError("Version number out of range");
    let e = (16 * t + 128) * t + 64;
    if (t >= 2) {
      const r = Math.floor(t / 7) + 2;
      e -= (25 * r - 10) * r - 55, t >= 7 && (e -= 36);
    }
    return assert(e >= 208 && e <= 29648), e;
  }
  static getNumDataCodewords(t, e) {
    return Math.floor(_QrCode.getNumRawDataModules(t) / 8) - _QrCode.ECC_CODEWORDS_PER_BLOCK[e.ordinal][t] * _QrCode.NUM_ERROR_CORRECTION_BLOCKS[e.ordinal][t];
  }
  static reedSolomonComputeDivisor(t) {
    if (t < 1 || t > 255) throw new RangeError("Degree out of range");
    const e = [];
    for (let r2 = 0; r2 < t - 1; r2++) e.push(0);
    e.push(1);
    let r = 1;
    for (let o = 0; o < t; o++) {
      for (let t2 = 0; t2 < e.length; t2++) e[t2] = _QrCode.reedSolomonMultiply(e[t2], r), t2 + 1 < e.length && (e[t2] ^= e[t2 + 1]);
      r = _QrCode.reedSolomonMultiply(r, 2);
    }
    return e;
  }
  static reedSolomonComputeRemainder(t, e) {
    const r = e.map(() => 0);
    for (const o of t) {
      const t2 = o ^ r.shift();
      r.push(0), e.forEach((e2, o2) => {
        r[o2] ^= _QrCode.reedSolomonMultiply(e2, t2);
      });
    }
    return r;
  }
  static reedSolomonMultiply(t, e) {
    if (t >>> 8 != 0 || e >>> 8 != 0) throw new RangeError("Byte out of range");
    let r = 0;
    for (let o = 7; o >= 0; o--) r = r << 1 ^ 285 * (r >>> 7), r ^= (e >>> o & 1) * t;
    return assert(r >>> 8 == 0), r;
  }
  finderPenaltyCountPatterns(t) {
    const e = t[1];
    assert(e <= 3 * this.size);
    const r = e > 0 && t[2] === e && t[3] === 3 * e && t[4] === e && t[5] === e;
    return (r && t[0] >= 4 * e && t[6] >= e ? 1 : 0) + (r && t[6] >= 4 * e && t[0] >= e ? 1 : 0);
  }
  finderPenaltyTerminateAndCount(t, e, r) {
    let o = e;
    return t && (this.finderPenaltyAddHistory(o, r), o = 0), o += this.size, this.finderPenaltyAddHistory(o, r), this.finderPenaltyCountPatterns(r);
  }
  finderPenaltyAddHistory(t, e) {
    let r = t;
    0 === e[0] && (r += this.size), e.pop(), e.unshift(r);
  }
};
QrCode.MIN_VERSION = 1, QrCode.MAX_VERSION = 40, QrCode.PENALTY_N1 = 3, QrCode.PENALTY_N2 = 3, QrCode.PENALTY_N3 = 40, QrCode.PENALTY_N4 = 10, QrCode.ECC_CODEWORDS_PER_BLOCK = [[-1, 7, 10, 15, 20, 26, 18, 20, 24, 30, 18, 20, 24, 26, 30, 22, 24, 28, 30, 28, 28, 28, 28, 30, 30, 26, 28, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30], [-1, 10, 16, 26, 18, 24, 16, 18, 22, 22, 26, 30, 22, 22, 24, 24, 28, 28, 26, 26, 26, 26, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28], [-1, 13, 22, 18, 26, 18, 24, 18, 22, 20, 24, 28, 26, 24, 20, 30, 24, 28, 28, 26, 30, 28, 30, 30, 30, 30, 28, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30], [-1, 17, 28, 22, 16, 22, 28, 26, 26, 24, 28, 24, 28, 22, 24, 24, 30, 28, 28, 26, 28, 30, 24, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30]], QrCode.NUM_ERROR_CORRECTION_BLOCKS = [[-1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 4, 4, 4, 4, 4, 6, 6, 6, 6, 7, 8, 8, 9, 9, 10, 12, 12, 12, 13, 14, 15, 16, 17, 18, 19, 19, 20, 21, 22, 24, 25], [-1, 1, 1, 1, 2, 2, 4, 4, 4, 5, 5, 5, 8, 9, 9, 10, 10, 11, 13, 14, 16, 17, 17, 18, 20, 21, 23, 25, 26, 28, 29, 31, 33, 35, 37, 38, 40, 43, 45, 47, 49], [-1, 1, 1, 2, 2, 4, 4, 6, 6, 8, 8, 8, 10, 12, 16, 12, 17, 16, 18, 21, 20, 23, 23, 25, 27, 29, 34, 34, 35, 38, 40, 43, 45, 48, 51, 53, 56, 59, 62, 65, 68], [-1, 1, 1, 2, 4, 4, 4, 5, 6, 8, 8, 11, 11, 16, 16, 18, 16, 19, 21, 25, 25, 25, 34, 30, 32, 35, 37, 40, 42, 45, 48, 51, 54, 57, 60, 63, 66, 70, 74, 77, 81]];

// node_modules/tdesign-miniprogram/miniprogram_dist/common/shared/qrcode/utils.js
var ERROR_LEVEL_MAP = { L: Ecc.LOW, M: Ecc.MEDIUM, Q: Ecc.QUARTILE, H: Ecc.HIGH };
var DEFAULT_SIZE = 160;
var DEFAULT_LEVEL = "M";
var DEFAULT_BACKGROUND_COLOR = "#FFFFFF";
var DEFAULT_FRONT_COLOR = "#000000";
var DEFAULT_NEED_MARGIN = false;
var DEFAULT_MINVERSION = 1;
var SPEC_MARGIN_SIZE = 4;
var DEFAULT_MARGIN_SIZE = 0;
var DEFAULT_IMG_SCALE = 0.1;
var generatePath = (t, o = 0) => {
  const e = [];
  return t.forEach((t2, n) => {
    let r = null;
    t2.forEach((c, l) => {
      if (!c && null !== r) return e.push(`M${r + o} ${n + o}h${l - r}v1H${r + o}z`), void (r = null);
      if (l !== t2.length - 1) c && null === r && (r = l);
      else {
        if (!c) return;
        null === r ? e.push(`M${l + o},${n + o} h1v1H${l + o}z`) : e.push(`M${r + o},${n + o} h${l + 1 - r}v1H${r + o}z`);
      }
    });
  }), e.join("");
};
var excavateModules = (t, o) => t.slice().map((t2, e) => e < o.y || e >= o.y + o.h ? t2 : t2.map((t3, e2) => (e2 < o.x || e2 >= o.x + o.w) && t3));
var getImageSettings = (t, o, e, n) => {
  if (null == n) return null;
  const r = t.length + 2 * e, c = Math.floor(0.1 * o), l = r / o, a = (n.width || c) * l, h = (n.height || c) * l, s = null == n.x ? t.length / 2 - a / 2 : n.x * l, E = null == n.y ? t.length / 2 - h / 2 : n.y * l, p = null == n.opacity ? 1 : n.opacity;
  let i = null;
  if (n.excavate) {
    const t2 = Math.floor(s), o2 = Math.floor(E);
    i = { x: t2, y: o2, w: Math.ceil(a + s - t2), h: Math.ceil(h + E - o2) };
  }
  const { crossOrigin: x } = n;
  return { x: s, y: E, h, w: a, excavation: i, opacity: p, crossOrigin: x };
};
var getMarginSize = (t, o) => null != o ? Math.max(Math.floor(o), 0) : t ? 4 : 0;
var isSupportPath2d = (() => {
  try {
    new Path2D().addPath(new Path2D());
  } catch (t) {
    return false;
  }
  return true;
})();

// node_modules/tdesign-miniprogram/miniprogram_dist/qrcode/components/qrcode-canvas/props.js
var props_default = { value: { type: String, value: "" }, icon: { type: String, value: "" }, size: { type: Number, value: 160 }, iconSize: { type: null, value: 40 }, level: { type: String, value: "M" }, bgColor: { type: String, value: DEFAULT_BACKGROUND_COLOR }, color: { type: String, value: DEFAULT_FRONT_COLOR }, includeMargin: { type: Boolean, value: DEFAULT_NEED_MARGIN }, marginSize: { type: Number, value: DEFAULT_MARGIN_SIZE } };

// node_modules/tdesign-miniprogram/miniprogram_dist/qrcode/hooks/useQRCode.js
var useQRCode = (e) => {
  const { value: t, level: n, minVersion: r, includeMargin: g, marginSize: o, imageSettings: m, size: i } = e, s = (() => {
    const e2 = QrSegment.makeSegments(t);
    return QrCode.encodeSegments(e2, ERROR_LEVEL_MAP[n], r);
  })(), a = s.getModules(), d = getMarginSize(g, o), c = getImageSettings(a, i, d, m);
  return { cells: a, margin: d, numCells: a.length + 2 * d, calculatedImageSettings: c, qrcode: s };
};
var useQRCode_default = useQRCode;

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

// node_modules/tdesign-miniprogram/miniprogram_dist/qrcode/components/qrcode-canvas/qrcode-canvas.js
var QRCode = class extends SuperComponent {
  constructor() {
    super(...arguments), this.properties = props_default, this.lifetimes = { ready() {
      this.checkDefaultValue(), this.initCanvas();
    } }, this.observers = { "**": function() {
      this.checkDefaultValue(), this.initCanvas();
    } }, this.methods = { initCanvas() {
      return __awaiter(this, void 0, void 0, function* () {
        this.createSelectorQuery().select("#qrcodeCanvas").fields({ node: true, size: true }).exec((e) => __awaiter(this, void 0, void 0, function* () {
          var t;
          if (!(null === (t = e[0]) || void 0 === t ? void 0 : t.node)) return;
          const i = e[0].node, o = i.getContext("2d");
          yield this.drawQrcode(i, o);
        }));
      });
    }, drawQrcode(e, t) {
      var i;
      return __awaiter(this, void 0, void 0, function* () {
        if (!t) return;
        const { value: o, icon: r, size: a, iconSize: l, level: s, bgColor: n, color: c, includeMargin: d, marginSize: h } = this.properties, u = this.getSizeProp(l);
        try {
          const l2 = useQRCode_default({ value: o, level: s, minVersion: DEFAULT_MINVERSION, includeMargin: d, marginSize: h, size: a, imageSettings: r ? { src: r, width: u.width, height: u.height, excavate: true } : void 0 }), g = wx.getWindowInfo().pixelRatio || 1;
          e.width = a * g, e.height = a * g;
          const m = a * g / l2.numCells;
          t.scale(m, m), t.fillStyle = n, t.fillRect(0, 0, l2.numCells, l2.numCells);
          let v = l2.cells;
          if (r && (null === (i = l2.calculatedImageSettings) || void 0 === i ? void 0 : i.excavation) && (v = excavateModules(l2.cells, l2.calculatedImageSettings.excavation)), t.fillStyle = c, v.forEach((e2, i2) => {
            e2.forEach((e3, o2) => {
              e3 && t.fillRect(o2 + l2.margin, i2 + l2.margin, 1.05, 1.05);
            });
          }), r && l2.calculatedImageSettings) {
            const i2 = e.createImage();
            yield new Promise((e2, t2) => {
              i2.onload = e2, i2.onerror = t2, i2.src = this.properties.icon;
            }), t.drawImage(i2, l2.calculatedImageSettings.x + l2.margin, l2.calculatedImageSettings.y + l2.margin, l2.calculatedImageSettings.w, l2.calculatedImageSettings.h);
          }
          this.triggerEvent("drawCompleted");
        } catch (e2) {
          this.triggerEvent("drawError", { error: e2 });
        }
      });
    }, getSizeProp: (e) => e ? "number" == typeof e ? { width: e, height: e } : { width: e.width, height: e.height } : { width: 0, height: 0 }, checkDefaultValue() {
      const e = { bgColor: "", color: "" };
      let t = false;
      const { bgColor: i, color: o } = this.properties, { bgColor: r, color: a } = props_default;
      "" === i && r.value && (e.bgColor = r.value, t = true), "" === o && a.value && (e.color = a.value, t = true), t && this.setData(e);
    }, getCanvasNode() {
      return new Promise((e) => {
        this.createSelectorQuery().select("#qrcodeCanvas").fields({ node: true, size: true }).exec((t) => {
          var i;
          e(null === (i = t[0]) || void 0 === i ? void 0 : i.node);
        });
      });
    } };
  }
};
QRCode = __decorate([wxComponent()], QRCode);
var qrcode_canvas_default = QRCode;
