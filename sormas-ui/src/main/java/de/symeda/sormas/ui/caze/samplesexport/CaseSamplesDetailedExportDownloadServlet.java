package de.symeda.sormas.ui.caze.samplesexport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.ServletSecurity.EmptyRoleSemantic;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.samplesexport.CaseSamplesDetailedExportFileDto;

/**
 * Public download endpoint for detailed sample export files.
 * Access is granted by a time-limited token in the URL; login is optional.
 */
@WebServlet(urlPatterns = {
	"/export-downloads/*" })
@ServletSecurity(@HttpConstraint(EmptyRoleSemantic.PERMIT))
public class CaseSamplesDetailedExportDownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(CaseSamplesDetailedExportDownloadServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String pathInfo = req.getPathInfo();
		if (pathInfo != null && pathInfo.startsWith("/auth/")) {
			handleAuthenticatedDownload(req, resp, pathInfo.substring("/auth/".length()));
			return;
		}

		String rawToken = pathInfo != null ? StringUtils.strip(pathInfo, "/") : null;
		if (StringUtils.isBlank(rawToken) || rawToken.contains("/") || rawToken.contains("..")) {
			writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid download link.");
			return;
		}

		CaseSamplesDetailedExportFileDto fileDto;
		try {
			fileDto = FacadeProvider.getCaseSamplesDetailedExportFacade().resolveDownloadToken(rawToken);
		} catch (Exception e) {
			LOGGER.error("Failed to resolve download token", e);
			writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Download is temporarily unavailable.");
			return;
		}

		if (fileDto == null) {
			writeError(resp, HttpServletResponse.SC_NOT_FOUND, "This download link is invalid or has expired.");
			return;
		}

		serveFile(req, resp, fileDto);
	}

	private void handleAuthenticatedDownload(HttpServletRequest req, HttpServletResponse resp, String exportUuid) throws IOException {
		if (req.getUserPrincipal() == null) {
			writeError(resp, HttpServletResponse.SC_UNAUTHORIZED, "You must be logged in to download this file.");
			return;
		}
		if (StringUtils.isBlank(exportUuid) || exportUuid.contains("/") || exportUuid.contains("..")) {
			writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid export identifier.");
			return;
		}

		CaseSamplesDetailedExportFileDto fileDto;
		try {
			fileDto = FacadeProvider.getCaseSamplesDetailedExportFacade().getFileForAuthenticatedUser(exportUuid);
		} catch (Exception e) {
			LOGGER.error("Failed to resolve authenticated export download for {}", exportUuid, e);
			writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Download is temporarily unavailable.");
			return;
		}

		if (fileDto == null) {
			writeError(resp, HttpServletResponse.SC_NOT_FOUND, "This export is not available or has expired.");
			return;
		}

		serveFile(req, resp, fileDto);
	}

	private void serveFile(HttpServletRequest req, HttpServletResponse resp, CaseSamplesDetailedExportFileDto fileDto) throws IOException {
		File file = new File(fileDto.getAbsoluteFilePath());
		if (!file.isFile()) {
			writeError(resp, HttpServletResponse.SC_NOT_FOUND, "The export file is no longer available.");
			return;
		}

		String downloadingUserName = resolveLoggedInUserName(req);
		try {
			FacadeProvider.getCaseSamplesDetailedExportFacade().recordDownload(fileDto.getExportUuid(), downloadingUserName);
		} catch (Exception e) {
			LOGGER.warn("Failed to record download for export {}", fileDto.getExportUuid(), e);
		}

		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("text/csv; charset=UTF-8");
		resp.setHeader("Content-Disposition", "attachment; filename=\"" + sanitizeFileName(fileDto.getFileName()) + "\"");
		resp.setContentLengthLong(file.length());

		try (InputStream in = new FileInputStream(file); OutputStream out = resp.getOutputStream()) {
			IOUtils.copy(in, out);
			out.flush();
		}
	}

	private static String resolveLoggedInUserName(HttpServletRequest req) {
		Principal principal = req.getUserPrincipal();
		if (principal == null || StringUtils.isBlank(principal.getName())) {
			return null;
		}
		return principal.getName();
	}

	private static String sanitizeFileName(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return "export.csv";
		}
		return fileName.replaceAll("[\\\\/\"'\\r\\n]", "_");
	}

	private static void writeError(HttpServletResponse resp, int status, String message) throws IOException {
		resp.setStatus(status);
		resp.setContentType("text/html; charset=UTF-8");
		String body = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"/><title>Export download</title></head>"
			+ "<body><h1>Export download</h1><p>" + message + "</p></body></html>";
		resp.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
	}
}
