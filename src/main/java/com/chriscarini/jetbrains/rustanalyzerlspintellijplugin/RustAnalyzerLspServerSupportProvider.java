package com.chriscarini.jetbrains.rustanalyzerlspintellijplugin;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.lsp.api.LspServerSupportProvider;
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;


class RustAnalyzerLspServerSupportProvider implements LspServerSupportProvider {

  public static final String RUST_EXTENSION = "rs";

  @Override
  public void fileOpened(@NotNull Project project, @NotNull VirtualFile virtualFile,
      @NotNull LspServerSupportProvider.LspServerStarter lspServerStarter) {
    if (!isJBRustPluginEnabled() && Objects.equals(virtualFile.getExtension(), RUST_EXTENSION)) {
      lspServerStarter.ensureServerStarted(new RustAnalyzerLspServerDescriptor(project));
    }
  }

  private static class RustAnalyzerLspServerDescriptor extends ProjectWideLspServerDescriptor {
    public RustAnalyzerLspServerDescriptor(@NotNull Project project) {
      super(project, "rust-analyzer");
    }

    @Override
    public boolean isSupportedFile(@NotNull VirtualFile virtualFile) {
      if (isJBRustPluginEnabled()) {
        return false;
      }
      return Objects.equals(virtualFile.getExtension(), RUST_EXTENSION);
    }

    @NotNull
    @Override
    public GeneralCommandLine createCommandLine() {
      return new GeneralCommandLine("/opt/homebrew/bin/rust-analyzer");
    }
  }

  private static boolean isJBRustPluginEnabled() {
    final PluginId jbRustPluginId = PluginId.getId("com.jetbrains.rust");
    boolean jbRustPluginInstalled = PluginManager.isPluginInstalled(jbRustPluginId);
    final IdeaPluginDescriptor jbRustPlugin = PluginManagerCore.getPlugin(jbRustPluginId);

    if (jbRustPlugin == null) {
      return false;
    }

    boolean jbRustPluginLoaded = PluginManagerCore.isLoaded(jbRustPlugin.getPluginId());
    boolean jbRustPluginEnabled = !PluginManagerCore.isDisabled(jbRustPlugin.getPluginId());

    return jbRustPluginInstalled && jbRustPluginLoaded && jbRustPluginEnabled;
  }
}
