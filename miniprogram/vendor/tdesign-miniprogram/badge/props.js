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

// node_modules/tdesign-miniprogram/miniprogram_dist/badge/props.js
var props_exports = {};
__export(props_exports, {
  default: () => props_default
});
module.exports = __toCommonJS(props_exports);
var props = { color: { type: String, value: "" }, content: { type: String, value: "" }, count: { type: null, value: 0 }, dot: { type: Boolean, value: false }, maxCount: { type: Number, value: 99 }, offset: { type: Array }, shape: { type: String, value: "circle" }, showZero: { type: Boolean, value: false }, size: { type: String, value: "medium" } };
var props_default = props;
