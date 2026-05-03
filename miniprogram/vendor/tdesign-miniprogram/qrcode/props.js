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

// node_modules/tdesign-miniprogram/miniprogram_dist/qrcode/props.js
var props_exports = {};
__export(props_exports, {
  default: () => props_default
});
module.exports = __toCommonJS(props_exports);
var props = { bgColor: { type: String, value: "" }, borderless: { type: Boolean, value: false }, color: { type: String, value: "" }, icon: { type: String, value: "" }, iconSize: { type: null, value: 40 }, level: { type: String, value: "M" }, size: { type: Number, value: 160 }, status: { type: String, value: "active" }, value: { type: String, value: "" } };
var props_default = props;
