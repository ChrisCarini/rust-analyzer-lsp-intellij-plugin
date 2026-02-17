package com.chriscarini.jetbrains.rustanalyzerlspintellijplugin;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.lsp.api.LspServerSupportProvider;
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;


class RustAnalyzerLspServerSupportProvider implements LspServerSupportProvider {

  public static final String RUST_EXTENSION = "rs";
  private static final String NOTIFICATION_GROUP_ID = "rust-analyzer LSP";

  // Flag to prevent duplicate notifications
  private static final AtomicBoolean notificationShown = new AtomicBoolean(false);

  @Override
  public void fileOpened(@NotNull Project project, @NotNull VirtualFile virtualFile,
      @NotNull LspServerSupportProvider.LspServerStarter lspServerStarter) {
    if (!isJBRustPluginEnabled() && Objects.equals(virtualFile.getExtension(), RUST_EXTENSION)) {
      if (!isRustAnalyzerPathValid()) {
        showConfigureNotification(project);
        return;
      }
      lspServerStarter.ensureServerStarted(new RustAnalyzerLspServerDescriptor(project));
    }
  }

  /**
   * Check if the rust-analyzer path is valid and the executable exists.
   */
  private static boolean isRustAnalyzerPathValid() {
    String path = RustAnalyzerSettings.getInstance().getRustAnalyzerPath();
    if (path == null || path.trim().isEmpty() || path.equals("rust-analyzer")) {
      // Check if rust-analyzer is available in PATH
      return isCommandAvailable("rust-analyzer");
    }
    File file = new File(path);
    return file.exists() && file.isFile() && file.canExecute();
  }

  /**
   * Check if a command is available in the system PATH.
   */
  private static boolean isCommandAvailable(String command) {
    try {
      ProcessBuilder pb = new ProcessBuilder(command, "--version");
      pb.redirectErrorStream(true);
      Process process = pb.start();
      int exitCode = process.waitFor();
      return exitCode == 0;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Show a notification prompting the user to configure the rust-analyzer path.
   * Only shows once until dismissed or settings configured.
   */
  private static void showConfigureNotification(@NotNull Project project) {
    // Prevent duplicate notifications
    if (!notificationShown.compareAndSet(false, true)) {
      return;
    }

    Notification notification = NotificationGroupManager.getInstance()
        .getNotificationGroup(NOTIFICATION_GROUP_ID)
        .createNotification(
            "rust-analyzer LSP",
            "rust-analyzer executable not found. Please configure the path in settings.",
            NotificationType.WARNING
        );

    notification.addAction(new NotificationAction("Configure Settings") {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
        ShowSettingsUtil.getInstance().showSettingsDialog(
            project,
            "rust-analyzer LSP"
        );
        notification.expire();
      }
    });

    // Reset flag when notification expires so it can show again if needed
    notification.whenExpired(() -> notificationShown.set(false));

    notification.notify(project);
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
      String rustAnalyzerPath = RustAnalyzerSettings.getInstance().getRustAnalyzerPath();
      return new GeneralCommandLine(rustAnalyzerPath);
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
