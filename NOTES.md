# Notes

## TODO

- [ ] Look into filetype icon replacement (see section below)
- [ ] Look into plugin replacement (see section below)
- [ ] Publish to marketplace
    - [ ] Update README.md to match other plugins (badges)
    - [ ] Add screenshots

## Info

### Replace filetype icon

- https://platform.jetbrains.com/t/how-to-make-a-plugin-that-changes-a-file-icon/466/3?u=chriscarini

### Plugin Replacement

`PluginReplacement` is an interface that allows you to replace a plugin with another plugin.

- **Implementation:
  ** https://github.com/JetBrains/intellij-community/blob/idea/243.25659.39/platform/platform-api/src/com/intellij/ide/plugins/PluginReplacement.java
- **NOTE:** Might also need to IMPL/call `setPluginDescriptor()` method in constructor to set old plugin descriptor
- **IntelliJ Platform Explorer:** https://plugins.jetbrains.com/intellij-platform-explorer/extensions?extensions=com.intellij.pluginReplacement
- **Examples**
    - `nx-console-idea-plugin` - https://plugins.jetbrains.com/plugin/15101-nx-console-idea
        - https://github.com/iguissouma/nx-console-idea-plugin/blob/6fc42d2b7c3e9cbc5840e609d75938d07b38d91b/src/main/kotlin/com/github/iguissouma/nxconsole/deprecation/NxConsolePluginReplacement.kt#L4
        - **Replaces:** https://plugins.jetbrains.com/plugin/21060-nx-console
    - `Markdown Navigator Enhanced` - https://plugins.jetbrains.com/plugin/7896-markdown-navigator-enhanced
        - https://github.com/vsch/idea-multimarkdown/blob/ec413c0e1b784ff7309ef073ddb4907d04345073/src/main/java/com/vladsch/md/nav/MdMarkdownPluginReplacement.java#L5
        - **Replaces:** https://plugins.jetbrains.com/plugin/7896-markdown-navigator-enhanced (NOTE; THIS IS THE SAME ID AS WHAT IS IN `PluginReplacement` -
          CONFUSING...)
        - https://github.com/search?q=repo%3Avsch%2Fidea-multimarkdown+MdMarkdownPluginReplacement&type=code
- https://plugins.jetbrains.com/search?excludeTags=internal&products=androidstudio&products=appcode&products=aqua&products=clion&products=dataspell&products=dbe&products=fleet&products=go&products=idea&products=idea_ce&products=mps&products=phpstorm&products=pycharm&products=pycharm_ce&products=rider&products=ruby&products=rust&products=webstorm&products=writerside&search=markdown
- https://plugins.jetbrains.com/plugin/7793-markdown
- [IDEs Support (IntelliJ Platform) | JetBrains - "How to handle plugin conflicts"](https://intellij-support.jetbrains.com/hc/en-us/community/posts/360007078259-How-to-handle-plugin-conflicts)

### File Type

- https://www.google.com/search?q=intellij+plugin+sdk+extension+point+how+to+get+plugin+to+show+up+as+supported+for+a+filetype
- https://plugins.jetbrains.com/docs/marketplace/intellij-plugin-recommendations.html#file-type
- https://plugins.jetbrains.com/feature/
- https://intellij-support.jetbrains.com/hc/en-us/community/posts/6294749129106-Plugin-development-How-to-get-extension-names-of-all-supported-file-types

### JetBrains Rust Plugin

- https://plugins.jetbrains.com/plugin/22407-rust