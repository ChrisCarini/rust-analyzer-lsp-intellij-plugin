package com.chriscarini.jetbrains.rustanalyzerlspintellijplugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.platform.lsp.api.LspServerManager;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Settings UI for the rust-analyzer LSP plugin.
 */
public class RustAnalyzerSettingsConfigurable implements Configurable {

  private TextFieldWithBrowseButton rustAnalyzerPathField;
  private JBLabel statusLabel;

  @Override
  public @NlsContexts.ConfigurableName String getDisplayName() {
    return "rust-analyzer LSP";
  }

  @SuppressWarnings("deprecation")
  @Override
  public @Nullable JComponent createComponent() {
    FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor()
        .withTitle("Select rust-analyzer Executable")
        .withDescription("Choose the path to the rust-analyzer executable");

    rustAnalyzerPathField = new TextFieldWithBrowseButton();
    rustAnalyzerPathField.addBrowseFolderListener(null, descriptor);

    // Add listener to validate path on change
    rustAnalyzerPathField.getTextField().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
      @Override
      public void insertUpdate(javax.swing.event.DocumentEvent e) {
        updateStatus();
      }

      @Override
      public void removeUpdate(javax.swing.event.DocumentEvent e) {
        updateStatus();
      }

      @Override
      public void changedUpdate(javax.swing.event.DocumentEvent e) {
        updateStatus();
      }
    });

    statusLabel = new JBLabel();

    // Help text for finding rust-analyzer path
    JBLabel helpLabel = new JBLabel("<html>" +
        "<b>How to find rust-analyzer path:</b><br>" +
        "<br>" +
        "<b>Windows CMD:</b> where rust-analyzer<br>" +
        "<b>Windows PowerShell:</b> (Get-Command rust-analyzer).Source<br>" +
        "<b>macOS/Linux:</b> which rust-analyzer<br>" +
        "<br>" +
        "<b>Install via rustup (recommended):</b><br>" +
        "&nbsp;&nbsp;rustup component add rust-analyzer<br>" +
        "<br>" +
        "<b>Or download from:</b><br>" +
        "&nbsp;&nbsp;https://github.com/rust-lang/rust-analyzer/releases" +
        "</html>");
    helpLabel.setForeground(Color.GRAY);

    JPanel panel = FormBuilder.createFormBuilder()
        .addLabeledComponent(
            "rust-analyzer path:",
            rustAnalyzerPathField,
            1,
            false
        )
        .addComponent(statusLabel)
        .addSeparator(10)
        .addComponent(helpLabel)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();

    // Initial status update
    SwingUtilities.invokeLater(this::updateStatus);

    return panel;
  }

  private void updateStatus() {
    String path = rustAnalyzerPathField.getText().trim();
    if (path.isEmpty() || path.equals("rust-analyzer")) {
      // Check if rust-analyzer is available in PATH
      if (isCommandAvailable("rust-analyzer")) {
        statusLabel.setText("Using rust-analyzer from system PATH");
        statusLabel.setForeground(new Color(0, 128, 0)); // Green
      } else {
        statusLabel.setText("rust-analyzer not found in system PATH. Please specify the full path.");
        statusLabel.setForeground(Color.RED);
      }
    } else {
      File file = new File(path);
      if (file.exists() && file.isFile()) {
        statusLabel.setText("Path is valid");
        statusLabel.setForeground(new Color(0, 128, 0)); // Green
      } else if (file.exists() && file.isDirectory()) {
        statusLabel.setText("Path is a directory, not an executable file");
        statusLabel.setForeground(Color.RED);
      } else {
        statusLabel.setText("File does not exist");
        statusLabel.setForeground(Color.RED);
      }
    }
  }

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

  @Override
  public boolean isModified() {
    RustAnalyzerSettings settings = RustAnalyzerSettings.getInstance();
    String currentPath = settings.getRustAnalyzerPath();
    String newPath = rustAnalyzerPathField.getText().trim();

    if (newPath.isEmpty()) {
      newPath = "rust-analyzer";
    }

    return !currentPath.equals(newPath);
  }

  @Override
  public void apply() throws ConfigurationException {
    String path = rustAnalyzerPathField.getText().trim();
    if (path.isEmpty()) {
      path = "rust-analyzer";
    }

    // Validate path before saving
    if (!path.equals("rust-analyzer")) {
      File file = new File(path);
      if (!file.exists()) {
        throw new ConfigurationException("The specified path does not exist: " + path);
      }
      if (!file.isFile()) {
        throw new ConfigurationException("The specified path is not a file: " + path);
      }
    } else {
      // Check if rust-analyzer is available in PATH
      if (!isCommandAvailable("rust-analyzer")) {
        throw new ConfigurationException(
            "rust-analyzer is not found in system PATH. Please specify the full path to rust-analyzer executable."
        );
      }
    }

    RustAnalyzerSettings settings = RustAnalyzerSettings.getInstance();
    settings.setRustAnalyzerPath(path);

    // Restart LSP servers for all open projects
    restartLspServers();
  }

  /**
   * Restart LSP servers for all open projects.
   */
  @SuppressWarnings("UnstableApiUsage")
  private void restartLspServers() {
    ApplicationManager.getApplication().invokeLater(() -> {
      for (Project project : ProjectManager.getInstance().getOpenProjects()) {
        if (!project.isDisposed()) {
          LspServerManager lspServerManager = LspServerManager.getInstance(project);
          lspServerManager.stopAndRestartIfNeeded(RustAnalyzerLspServerSupportProvider.class);
        }
      }
    });
  }

  @Override
  public void reset() {
    RustAnalyzerSettings settings = RustAnalyzerSettings.getInstance();
    rustAnalyzerPathField.setText(settings.getRustAnalyzerPath());
    updateStatus();
  }

  @Override
  public void disposeUIResources() {
    rustAnalyzerPathField = null;
    statusLabel = null;
  }
}
