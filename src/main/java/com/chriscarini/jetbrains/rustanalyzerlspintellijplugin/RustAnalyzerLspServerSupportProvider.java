package com.chriscarini.jetbrains.rustanalyzerlspintellijplugin;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.lsp.api.LspIntegrationProvider;
import com.intellij.platform.lsp.api.ProjectWideLspClientDescriptor;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;


class RustAnalyzerLspServerSupportProvider implements LspIntegrationProvider {

  public static final String RUST_EXTENSION = "rs";

  @Override
  public void fileOpened(@NotNull Project project, @NotNull VirtualFile virtualFile,
      @NotNull LspIntegrationProvider.LspClientStarter clientStarter) {
    if (!isJBRustPluginEnabled() && Objects.equals(virtualFile.getExtension(), RUST_EXTENSION)) {
      clientStarter.ensureClientStarted(new RustAnalyzerLspServerDescriptor(project));
    }
  }

  private static class RustAnalyzerLspServerDescriptor extends ProjectWideLspClientDescriptor {
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
    if (!PluginManager.isPluginInstalled(jbRustPluginId)) {
      return false;
    }
    boolean jbRustPluginLoaded = PluginManagerCore.isLoaded(jbRustPluginId);
    boolean jbRustPluginEnabled = !PluginManagerCore.isDisabled(jbRustPluginId);
    return jbRustPluginLoaded && jbRustPluginEnabled;
  }
}
