package com.cline.clineintellijplugin.core;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Service(Service.Level.APP)
public final class ClineCoreService implements Disposable {

    private static final Logger LOG = Logger.getInstance(ClineCoreService.class);
    private ProcessHandler clineProcessHandler;

    public ClineCoreService() {
        LOG.info("ClineCoreService initialized");
        // In a real scenario, you might start the core process here or on-demand,
        // or provide a method to be called when the user first interacts with the plugin.
    }

    public void startClineCore() {
        if (clineProcessHandler != null && !clineProcessHandler.isProcessTerminated()) {
            LOG.info("Cline Core process is already running.");
            return;
        }
        LOG.info("Attempting to start Cline Core Node.js process (STUBBED)...");
        // Placeholder for GeneralCommandLine setup
        // Example:
        // GeneralCommandLine commandLine = new GeneralCommandLine("node", "path/to/cline/main.js");
        // commandLine.setWorkDirectory("path/to/cline/working_directory");
        // try {
        //     // For a real implementation, ensure Node.js is available and the script path is correct.
        //     // You might need to bundle Node.js or ask the user to configure its path.
        //     clineProcessHandler = new OSProcessHandler(commandLine);
        //     clineProcessHandler.startNotify();
        //     LOG.info("Cline Core process STUBBED successfully started (simulated).");
        //     // Add listeners for stdout/stderr if needed:
        //     // clineProcessHandler.addProcessListener(new ProcessAdapter() { ... });
        // } catch (ExecutionException e) {
        //     LOG.error("Failed to start Cline Core process (STUBBED)", e);
        // }
        // For now, we'll just log the attempt.
        LOG.warn("Actual process starting logic is commented out in this stub.");
    }

    public void stopClineCore() {
        if (clineProcessHandler != null && !clineProcessHandler.isProcessTerminated()) {
            LOG.info("Attempting to stop Cline Core process (STUBBED)...");
            // clineProcessHandler.destroyProcess(); // Sends SIGTERM/SIGKILL
            // Or, for graceful shutdown if the process supports it:
            // if (clineProcessHandler.detachIsDefault()) {
            //    clineProcessHandler.detachProcess(); // Sends SIGHUP on Unix, Ctrl-Break on Windows
            // } else {
            //    clineProcessHandler.destroyProcess();
            // }
            // clineProcessHandler = null; // Set to null after confirming termination
            LOG.info("Cline Core process STUBBED successfully stopped (simulated).");
            LOG.warn("Actual process stopping logic is commented out in this stub.");
            // Simulate termination for isCoreProcessRunning
            // In a real scenario, you'd wait for termination or use a ProcessListener
            // For this stub, to make isCoreProcessRunning reflect the stop call:
            // clineProcessHandler = null; // Or a mock that says it's terminated.
        } else {
            LOG.info("Cline Core process is not running or already terminated (STUBBED).");
        }
    }

    public boolean isCoreProcessRunning() {
        // This stub will always return false unless startClineCore is actually implemented
        // and clineProcessHandler is properly assigned and managed.
        // For a more realistic stub, you might set a boolean flag.
        boolean isRunning = clineProcessHandler != null && !clineProcessHandler.isProcessTerminated();
        LOG.info("Checking if Cline Core process is running (STUBBED): " + isRunning);
        return isRunning;
    }

    /**
     * Reads the content of a file specified by its absolute path.
     * This operation is performed under a read action.
     *
     * @param project   The current project (can be used for context, notifications).
     * @param filePath  The absolute path to the file.
     * @return The content of the file as a String, or null if the file cannot be read or doesn't exist.
     */
    @Nullable
    public String readFileContent(@NotNull Project project, @NotNull String filePath) {
        // Project parameter is included for good practice and potential future use (e.g. notifications)
        // but LocalFileSystem and FileDocumentManager are generally available without it for this specific task.
        LOG.debug("Attempting to read file content for: " + filePath + " (Project: " + project.getName() + ")");

        return ReadAction.compute(() -> {
            VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(filePath);

            if (virtualFile == null || !virtualFile.exists()) {
                LOG.warn("File not found or does not exist: " + filePath);
                return null;
            }

            if (virtualFile.isDirectory()) {
                LOG.warn("Path is a directory, not a file: " + filePath);
                return null;
            }

            // Check if the file is binary before attempting to get a Document
            // Note: This check might not be exhaustive for all binary types but is a good first step.
            // IntelliJ's FileTypeRegistry can also be used for more detailed checks if needed.
            if (virtualFile.getFileType().isBinary()) {
                LOG.warn("File is binary, cannot read as text: " + filePath);
                return null;
            }

            Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
            if (document != null) {
                LOG.info("Successfully read content from: " + filePath);
                return document.getText();
            } else {
                // This case might happen for very large files or if the file is not typically associated with a document.
                LOG.warn("Could not get document for virtual file (possibly too large or unusual type): " + filePath);
                // As a fallback for non-binary files where getDocument might fail,
                // you could try reading bytes directly, but this is often not what's desired for text.
                // For now, returning null if document is null.
                // Example: return new String(virtualFile.contentsToByteArray(), virtualFile.getCharset());
                return null;
            }
        });
    }

    @Override
    public void dispose() {
        LOG.info("Disposing ClineCoreService, ensuring Cline Core process is stopped (STUBBED).");
        stopClineCore();
    }
}
