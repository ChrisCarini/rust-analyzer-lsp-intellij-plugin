package com.chriscarini.jetbrains.rustanalyzerlspintellijplugin;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Settings UI for the rust-analyzer LSP plugin.
 */
public class RustAnalyzerSettingsConfigurable implements Configurable {

  private TextFieldWithBrowseButton rustAnalyzerPathField;

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

    return FormBuilder.createFormBuilder()
        .addLabeledComponent(
            "rust-analyzer path:",
            rustAnalyzerPathField,
            1,
            false
        )
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();
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
  public void apply() {
    RustAnalyzerSettings settings = RustAnalyzerSettings.getInstance();
    String path = rustAnalyzerPathField.getText().trim();
    if (path.isEmpty()) {
      path = "rust-analyzer";
    }
    settings.setRustAnalyzerPath(path);
  }

  @Override
  public void reset() {
    RustAnalyzerSettings settings = RustAnalyzerSettings.getInstance();
    rustAnalyzerPathField.setText(settings.getRustAnalyzerPath());
  }

  @Override
  public void disposeUIResources() {
    rustAnalyzerPathField = null;
  }
}
