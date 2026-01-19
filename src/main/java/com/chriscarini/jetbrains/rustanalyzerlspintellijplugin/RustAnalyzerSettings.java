package com.chriscarini.jetbrains.rustanalyzerlspintellijplugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Persistent settings for the rust-analyzer LSP plugin.
 */
@State(
    name = "RustAnalyzerSettings",
    storages = @Storage("rustAnalyzerLspSettings.xml")
)
public class RustAnalyzerSettings implements PersistentStateComponent<RustAnalyzerSettings> {

  /**
   * The path to the rust-analyzer executable.
   */
  public String rustAnalyzerPath = "rust-analyzer";

  public static RustAnalyzerSettings getInstance() {
    return ApplicationManager.getApplication().getService(RustAnalyzerSettings.class);
  }

  @Override
  public @Nullable RustAnalyzerSettings getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull RustAnalyzerSettings state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  /**
   * Gets the rust-analyzer executable path.
   * If the path is empty or null, returns the default "rust-analyzer".
   */
  @NotNull
  public String getRustAnalyzerPath() {
    if (rustAnalyzerPath == null || rustAnalyzerPath.trim().isEmpty()) {
      return "rust-analyzer";
    }
    return rustAnalyzerPath;
  }

  /**
   * Sets the rust-analyzer executable path.
   */
  public void setRustAnalyzerPath(@Nullable String path) {
    this.rustAnalyzerPath = path;
  }
}
