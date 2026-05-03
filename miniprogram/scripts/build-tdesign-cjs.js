const fs = require('fs')
const path = require('path')
const esbuild = require('esbuild')

const projectRoot = path.resolve(__dirname, '..')
const sourceRoot = path.join(projectRoot, 'node_modules', 'tdesign-miniprogram', 'miniprogram_dist')
const outputRoot = path.join(projectRoot, 'vendor', 'tdesign-miniprogram')
const allowedRoots = new Set([
  'badge',
  'button',
  'common',
  'icon',
  'loading',
  'qrcode',
  'sticky',
  'tab-panel',
  'tabs',
  'tag'
])

function removeDir(target) {
  fs.rmSync(target, { recursive: true, force: true })
}

function ensureDir(target) {
  fs.mkdirSync(target, { recursive: true })
}

function walk(dir) {
  return fs.readdirSync(dir, { withFileTypes: true }).flatMap((entry) => {
    const fullPath = path.join(dir, entry.name)
    if (entry.isDirectory()) {
      return walk(fullPath)
    }
    return [fullPath]
  })
}

function copyFile(sourceFile, outputFile) {
  ensureDir(path.dirname(outputFile))
  fs.copyFileSync(sourceFile, outputFile)
}

function shouldInclude(relativePath) {
  const [rootName] = relativePath.split(path.sep)
  return allowedRoots.has(rootName)
}

function shouldBuildJs(relativePath) {
  const [rootName] = relativePath.split(path.sep)
  if (rootName === 'common') {
    return false
  }
  return relativePath.endsWith('.js')
}

function buildJs(sourceFile, outputFile) {
  ensureDir(path.dirname(outputFile))
  esbuild.buildSync({
    entryPoints: [sourceFile],
    outfile: outputFile,
    bundle: true,
    format: 'cjs',
    platform: 'browser',
    target: ['es2018'],
    charset: 'utf8',
    legalComments: 'none',
    minify: false,
    sourcemap: false,
    treeShaking: false,
    logLevel: 'silent'
  })
}

function main() {
  if (!fs.existsSync(sourceRoot)) {
    throw new Error(`未找到 TDesign 源目录：${sourceRoot}`)
  }

  removeDir(outputRoot)
  ensureDir(outputRoot)

  walk(sourceRoot).forEach((filePath) => {
    const relativePath = path.relative(sourceRoot, filePath)
    const outputPath = path.join(outputRoot, relativePath)

    if (!shouldInclude(relativePath)) {
      return
    }

    if (relativePath.endsWith('.d.ts')) {
      return
    }

    if (shouldBuildJs(relativePath)) {
      buildJs(filePath, outputPath)
      return
    }

    copyFile(filePath, outputPath)
  })

  console.log(`TDesign 已转译到：${outputRoot}`)
}

main()
