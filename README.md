# <img src="./src/main/resources/META-INF/pluginIcon.svg" width="32" /> rust-analyzer LSP IntelliJ Plugin

[![GitHub License](https://img.shields.io/github/license/ChrisCarini/rust-analyzer-lsp-intellij-plugin?style=flat-square)](https://github.com/ChrisCarini/rust-analyzer-lsp-intellij-plugin/blob/master/LICENSE)
[![All Contributors](https://img.shields.io/github/all-contributors/ChrisCarini/rust-analyzer-lsp-intellij-plugin?color=ee8449&style=flat-square)](#contributors)
[![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/ChrisCarini/rust-analyzer-lsp-intellij-plugin/build.yml?branch=main&logo=GitHub&style=flat-square)](https://github.com/ChrisCarini/rust-analyzer-lsp-intellij-plugin/actions/workflows/build.yml)
[![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/ChrisCarini/rust-analyzer-lsp-intellij-plugin/compatibility.yml?branch=main&label=IntelliJ%20Plugin%20Compatibility&logo=GitHub&style=flat-square)](https://github.com/ChrisCarini/rust-analyzer-lsp-intellij-plugin/actions/workflows/compatibility.yml)

<!-- Plugin description -->
A plugin for JetBrains IntelliJ IDE providing a `rust-analyzer` LSP. This plugin is currently a PoC-only, and was initially created to explore IntelliJ's LSP support. It is not intended for production use. Feel free to open a GitHub issue if you'd like to see this plugin developed further.
<!-- Plugin description end -->

## Fork Changes (v0.2.3)

This fork adds **cross-platform support** with configurable rust-analyzer path.

### New Features

- **Configurable Path**: Settings → Tools → rust-analyzer LSP
- **Cross-Platform**: Works on Windows, macOS, and Linux
- **Real-time Validation**: Path status indicator (green/red)
- **Auto-detect**: Automatically finds rust-analyzer in system PATH
- **Notification**: Shows warning with "Configure Settings" button when path is invalid
- **Auto-restart**: LSP server restarts automatically after settings change
- **Help Text**: Commands for finding rust-analyzer path on different terminals

### How to Find rust-analyzer Path

| Terminal | Command |
|----------|---------|
| Windows CMD | `where rust-analyzer` |
| Windows PowerShell | `(Get-Command rust-analyzer).Source` |
| macOS/Linux | `which rust-analyzer` |

### Install rust-analyzer

```bash
# Via rustup (recommended)
rustup component add rust-analyzer

# Or download from GitHub releases
# https://github.com/rust-lang/rust-analyzer/releases
```

## Original Description

This plugin expects that [`rust-analyzer`, the implementation of LSP for Rust](https://rust-analyzer.github.io/), is installed and available in your system PATH, or configured via Settings → Tools → rust-analyzer LSP.

Tested in IntelliJ and Rider.

## Contributors

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/ChrisCarini"><img src="https://avatars.githubusercontent.com/u/6374067?v=4?s=100" width="100px;" alt="Chris Carini"/><br /><sub><b>Chris Carini</b></sub></a><br /><a href="#bug-ChrisCarini" title="Bug reports">🐛</a> <a href="#code-ChrisCarini" title="Code">💻</a> <a href="#doc-ChrisCarini" title="Documentation">📖</a> <a href="#example-ChrisCarini" title="Examples">💡</a> <a href="#ideas-ChrisCarini" title="Ideas, Planning, & Feedback">🤔</a> <a href="#maintenance-ChrisCarini" title="Maintenance">🚧</a> <a href="#question-ChrisCarini" title="Answering Questions">💬</a> <a href="#review-ChrisCarini" title="Reviewed Pull Requests">👀</a></td>
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->