package net.redstone233.tam.core.tool;

import net.fabricmc.loader.api.FabricLoader;
import net.redstone233.tam.TestAnnMod;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.concurrent.TimeUnit;

/**
 * 模组 jar 内部文件操作工具
 */
public final class ModJarZipExtractor {

    /** 是否覆盖已存在文件 */
    private static final boolean OVERWRITE = true;

    /**
         * 解压操作结果类，包含成功状态和耗时信息
         */
        public record ExtractResult(boolean success, long durationMillis, String message) {

        public String getFormattedDuration() {
                if (durationMillis < 1000) {
                    return durationMillis + "ms";
                } else {
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis);
                    long millis = durationMillis % 1000;
                    return String.format("%d.%03ds", seconds, millis);
                }
            }

            @Override
            public @NotNull String toString() {
                return String.format("ExtractResult{success=%s, duration=%s, message='%s'}",
                        success, getFormattedDuration(), message);
            }
        }

    /**
     * 命令触发：解压指定路径的zip文件到目标目录，并返回解压结果（包含耗时）
     *
     * @param modClass 模组主类（用于定位jar文件）
     * @param zipPathInJar jar内部的zip文件路径，例如 "assets/mymod/data.zip"
     * @param outputDir 解压目标目录
     * @param logger 模组的Logger实例
     * @return 解压结果，包含成功状态和耗时
     */
    public static ExtractResult extractSpecificZipOnCommandWithTime(Class<?> modClass, String zipPathInJar, String outputDir, Object logger) {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        String message = "";

        try {
            // 获取模组jar文件路径
            String jarPath = getJarPath(modClass);
            if (jarPath == null) {
                message = "无法定位模组jar文件路径";
                logError(logger, message);
                return new ExtractResult(false, System.currentTimeMillis() - startTime, message);
            }

            logInfo(logger, "模组jar路径: " + jarPath);
            logInfo(logger, "正在解压: " + zipPathInJar + " -> " + outputDir);

            success = extractSpecificZipFromJar(jarPath, zipPathInJar, outputDir, logger);
            message = success ? "解压成功" : "解压失败";

        } catch (Exception e) {
            message = "解压过程中出现错误: " + e.getMessage();
            logError(logger, message);
            e.fillInStackTrace();
        }

        long duration = System.currentTimeMillis() - startTime;
        return new ExtractResult(success, duration, message);
    }

    /**
     * 命令触发：复制jar内的单个文件到指定路径，并返回操作结果（包含耗时）
     *
     * @param modClass 模组主类（用于定位jar文件）
     * @param filePathInJar jar内部的文件路径，例如 "assets/mymod/tsconfig.json"
     * @param outputPath 输出文件路径（包含文件名）
     * @param logger 模组的Logger实例
     * @return 操作结果，包含成功状态和耗时
     */
    public static ExtractResult copyFileFromJarOnCommandWithTime(Class<?> modClass, String filePathInJar, String outputPath, Object logger) {
        return copyOrMoveFileFromJarWithTime(modClass, filePathInJar, outputPath, false, logger);
    }

    /**
     * 命令触发：移动jar内的单个文件到指定路径，并返回操作结果（包含耗时）
     *
     * @param modClass 模组主类（用于定位jar文件）
     * @param filePathInJar jar内部的文件路径，例如 "assets/mymod/tsconfig.json"
     * @param outputPath 输出文件路径（包含文件名）
     * @param logger 模组的Logger实例
     * @return 操作结果，包含成功状态和耗时
     */
    public static ExtractResult moveFileFromJarOnCommandWithTime(Class<?> modClass, String filePathInJar, String outputPath, Object logger) {
        return copyOrMoveFileFromJarWithTime(modClass, filePathInJar, outputPath, true, logger);
    }

    /**
     * 命令触发：执行所有预设的文件操作（用于 /announcement dump 命令），并返回操作结果（包含总耗时）
     *
     * @param modClass 模组主类
     * @param logger 模组的Logger实例
     * @return 操作结果，包含成功状态和总耗时
     */
    public static ExtractResult dumpAllResourcesOnCommandWithTime(Class<?> modClass, Object logger) {
        long startTime = System.currentTimeMillis();
        boolean allSuccess = true;
        int operationCount = 0;
        int successCount = 0;

        // 这里可以预设多个文件操作
        ExtractResult result;

        // 示例：解压 assets/mymod/data.zip
        result = extractSpecificZipOnCommandWithTime(modClass,
                "assets/"+ TestAnnMod.MOD_ID +"/node_modules.zip",
                FabricLoader.getInstance().getGameDir() + "/data/" + TestAnnMod.MOD_ID + "node_modules",
                logger);
        operationCount++;
        if (result.success()) successCount++;
        allSuccess &= result.success();

        // 示例：复制 tsconfig.json
        result = copyFileFromJarOnCommandWithTime(modClass,
                "assets/"+TestAnnMod.MOD_ID+"/tsconfig.json",
                FabricLoader.getInstance().getGameDir() + "/data/" + TestAnnMod.MOD_ID + "scripts/tsconfig.json",
                logger);
        operationCount++;
        if (result.success()) successCount++;
        allSuccess &= result.success();

        // 示例：复制其他配置文件
        /*
        result = copyFileFromJarOnCommandWithTime(modClass,
                "assets/mymod/settings.json",
                "./config/mymod/settings.json",
                logger);
        operationCount++;
        if (result.success()) successCount++;
        allSuccess &= result.success();
        */


        long totalDuration = System.currentTimeMillis() - startTime;
        String message = String.format("资源导出完成: %d/%d 个操作成功，总耗时 %s",
                successCount, operationCount, formatDuration(totalDuration));

        if (allSuccess) {
            logInfo(logger, message);
        } else {
            logWarn(logger, message);
        }

        return new ExtractResult(allSuccess, totalDuration, message);
    }

    /**
     * 复制或移动jar内的单个文件，并返回操作结果（包含耗时）
     */
    private static ExtractResult copyOrMoveFileFromJarWithTime(Class<?> modClass, String filePathInJar, String outputPath, boolean move, Object logger) {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        String message = "";

        try {
            // 获取模组jar文件路径
            String jarPath = getJarPath(modClass);
            if (jarPath == null) {
                message = "无法定位模组jar文件路径";
                logError(logger, message);
                return new ExtractResult(false, System.currentTimeMillis() - startTime, message);
            }

            logInfo(logger, "模组jar路径: " + jarPath);
            logInfo(logger, (move ? "移动" : "复制") + "文件: " + filePathInJar + " -> " + outputPath);

            success = copyOrMoveSpecificFileFromJar(jarPath, filePathInJar, outputPath, move, logger);
            message = success ? (move ? "移动成功" : "复制成功") : (move ? "移动失败" : "复制失败");

        } catch (Exception e) {
            message = "文件操作过程中出现错误: " + e.getMessage();
            logError(logger, message);
            e.fillInStackTrace();
        }

        long duration = System.currentTimeMillis() - startTime;
        return new ExtractResult(success, duration, message);
    }

    /**
     * 格式化持续时间
     */
    private static String formatDuration(long durationMillis) {
        if (durationMillis < 1000) {
            return durationMillis + "ms";
        } else {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis);
            long millis = durationMillis % 1000;
            return String.format("%d.%03ds", seconds, millis);
        }
    }

    /**
     * 从jar文件中复制或移动特定的单个文件
     */
    private static boolean copyOrMoveSpecificFileFromJar(String jarPath, String filePathInJar, String outputPath, boolean move, Object logger) throws IOException {
        // 规范化路径（确保使用正斜杠）
        String normalizedFilePath = filePathInJar.replace('\\', '/');

        try (JarFile jar = new JarFile(jarPath)) {
            JarEntry fileEntry = jar.getJarEntry(normalizedFilePath);

            if (fileEntry == null) {
                logError(logger, "在jar中未找到文件: " + normalizedFilePath);
                return false;
            }

            if (fileEntry.isDirectory()) {
                logError(logger, "指定路径是目录而非文件: " + normalizedFilePath);
                return false;
            }

            // 创建输出目录
            Path outputFile = Paths.get(outputPath);
            Files.createDirectories(outputFile.getParent());

            // 检查目标文件是否已存在
            if (Files.exists(outputFile) && !OVERWRITE) {
                logInfo(logger, "跳过已存在文件: " + outputPath);
                return true;
            }

            // 复制文件
            try (InputStream in = jar.getInputStream(fileEntry);
                 OutputStream out = Files.newOutputStream(outputFile)) {
                copy(in, out);
            }

            logInfo(logger, "成功" + (move ? "移动" : "复制") + " " + normalizedFilePath + " 到 " + outputPath);
            return true;
        }
    }

    /**
     * 从jar文件中解压特定的zip文件
     */
    private static boolean extractSpecificZipFromJar(String jarPath, String zipPathInJar, String outputDir, Object logger) throws IOException {
        // 规范化路径（确保使用正斜杠）
        String normalizedZipPath = zipPathInJar.replace('\\', '/');

        try (JarFile jar = new JarFile(jarPath)) {
            JarEntry zipEntry = jar.getJarEntry(normalizedZipPath);

            if (zipEntry == null) {
                logError(logger, "在jar中未找到文件: " + normalizedZipPath);
                // 尝试列出所有条目来帮助调试
                logInfo(logger, "jar中的文件列表:");
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry je = entries.nextElement();
                    if (!je.isDirectory()) {
                        logInfo(logger, "  - " + je.getName());
                    }
                }
                return false;
            }

            if (zipEntry.isDirectory()) {
                logError(logger, "指定路径是目录而非zip文件: " + normalizedZipPath);
                return false;
            }

            if (!normalizedZipPath.toLowerCase().endsWith(".zip")) {
                logError(logger, "指定文件不是zip文件: " + normalizedZipPath);
                return false;
            }

            // 解压zip文件
            try (InputStream in = jar.getInputStream(zipEntry)) {
                unzipStream(in, outputDir, logger);
            }

            logInfo(logger, "成功解压 " + normalizedZipPath + " 到 " + outputDir);
            return true;
        }
    }

    /**
     * 获取类所在的jar文件路径
     */
    private static String getJarPath(Class<?> clazz) {
        try {
            String className = clazz.getSimpleName() + ".class";
            String classPath = Objects.requireNonNull(clazz.getResource(className)).toString();

            if (classPath.startsWith("jar:file:")) {
                // 从jar中运行
                int exclamation = classPath.indexOf('!');
                if (exclamation != -1) {
                    String jarPath = classPath.substring(9, exclamation);
                    return new File(jarPath).getAbsolutePath();
                }
            } else if (classPath.startsWith("file:")) {
                // 从开发环境运行
                String classFilePath = classPath.substring(5);
                int classDirEnd = classFilePath.indexOf(className);
                if (classDirEnd != -1) {
                    String classDir = classFilePath.substring(0, classDirEnd);
                    // 在开发环境中，返回classes目录的父目录（通常是项目根目录）
                    return new File(classDir).getParentFile().getParentFile().getAbsolutePath();
                }
            }
        } catch (Exception e) {
            // 这里不使用logger，因为logger可能还未传入
            System.err.println("获取jar路径时出错: " + e.getMessage());
        }
        return null;
    }

    /**
     * 把输入流当作 zip 解压到指定目录
     */
    private static void unzipStream(InputStream zipStream, String destDir, Object logger) throws IOException {
        Path root = Paths.get(destDir);
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }

        try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(zipStream))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path file = root.resolve(entry.getName()).normalize();
                if (!file.startsWith(root)) {
                    throw new IOException("Bad zip entry: " + entry.getName());
                }
                if (entry.isDirectory()) {
                    Files.createDirectories(file);
                } else {
                    if (Files.exists(file) && !OVERWRITE) {
                        logInfo(logger, "跳过已存在: " + file);
                        continue;
                    }
                    Files.createDirectories(file.getParent());
                    try (OutputStream out = Files.newOutputStream(file)) {
                        copy(zis, out);
                    }
                    logInfo(logger, "解压文件: " + file);
                }
                zis.closeEntry();
            }
        }
    }

    /**
     * 如果 jar 里 zip 路径带目录，只取文件名
     */
    private static String fileNameWithoutPath(String fullName) {
        int slash = fullName.lastIndexOf('/');
        return slash >= 0 ? fullName.substring(slash + 1) : fullName;
    }

    /**
     * 简易流拷贝
     */
    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[8192];
        int len;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
    }

    /**
     * 使用LOGGER输出信息
     */
    private static void logInfo(Object logger, String message) {
        try {
            // 使用反射调用LOGGER的info方法
            logger.getClass().getMethod("info", String.class).invoke(logger, message);
        } catch (Exception e) {
            // 如果反射失败，使用默认输出
            System.out.println("[INFO] " + message);
        }
    }

    /**
     * 使用LOGGER输出警告
     */
    private static void logWarn(Object logger, String message) {
        try {
            // 使用反射调用LOGGER的warn方法
            logger.getClass().getMethod("warn", String.class).invoke(logger, message);
        } catch (Exception e) {
            // 如果反射失败，使用默认输出
            System.out.println("[WARN] " + message);
        }
    }

    /**
     * 使用LOGGER输出错误
     */
    private static void logError(Object logger, String message) {
        try {
            // 使用反射调用LOGGER的error方法
            logger.getClass().getMethod("error", String.class).invoke(logger, message);
        } catch (Exception e) {
            // 如果反射失败，使用默认输出
            System.err.println("[ERROR] " + message);
        }
    }

    // ------------------ 保留原有方法（向后兼容） ------------------

    /**
     * 入口：把 jar 里所有 zip 解压到指定目录
     *
     * @param jarPath  模组 jar 绝对路径
     * @param destDir  想要解压到的目录（不存在会创建）
     * @param logger 模组的Logger实例
     */
    public static void extractAllZipInJar(String jarPath, String destDir, Object logger) throws IOException {
        Path dest = Paths.get(destDir);
        if (!Files.exists(dest)) {
            Files.createDirectories(dest);
        }

        try (JarFile jar = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry je = entries.nextElement();
                if (je.isDirectory()) {
                    continue;
                }
                String name = je.getName();
                // 只处理 jar 里的 zip
                if (name.toLowerCase().endsWith(".zip")) {
                    logInfo(logger, "发现嵌套 zip: " + name);
                    // 用 ZipInputStream 解压
                    try (InputStream in = jar.getInputStream(je)) {
                        unzipStream(in, dest.resolve(fileNameWithoutPath(name)).toString(), logger);
                    }
                }
            }
        }
    }

    /**
     * 命令触发：解压指定路径的zip文件到目标目录
     *
     * @param modClass 模组主类（用于定位jar文件）
     * @param zipPathInJar jar内部的zip文件路径，例如 "assets/mymod/data.zip"
     * @param outputDir 解压目标目录
     * @param logger 模组的Logger实例
     * @return 是否成功解压
     */
    public static boolean extractSpecificZipOnCommand(Class<?> modClass, String zipPathInJar, String outputDir, Object logger) {
        ExtractResult result = extractSpecificZipOnCommandWithTime(modClass, zipPathInJar, outputDir, logger);
        return result.success();
    }

    /**
     * 命令触发：复制jar内的单个文件到指定路径
     *
     * @param modClass 模组主类（用于定位jar文件）
     * @param filePathInJar jar内部的文件路径，例如 "assets/mymod/tsconfig.json"
     * @param outputPath 输出文件路径（包含文件名）
     * @param logger 模组的Logger实例
     * @return 是否成功复制
     */
    public static boolean copyFileFromJarOnCommand(Class<?> modClass, String filePathInJar, String outputPath, Object logger) {
        ExtractResult result = copyFileFromJarOnCommandWithTime(modClass, filePathInJar, outputPath, logger);
        return result.success();
    }

    /**
     * 命令触发：移动jar内的单个文件到指定路径
     *
     * @param modClass 模组主类（用于定位jar文件）
     * @param filePathInJar jar内部的文件路径，例如 "assets/mymod/tsconfig.json"
     * @param outputPath 输出文件路径（包含文件名）
     * @param logger 模组的Logger实例
     * @return 是否成功移动
     */
    public static boolean moveFileFromJarOnCommand(Class<?> modClass, String filePathInJar, String outputPath, Object logger) {
        ExtractResult result = moveFileFromJarOnCommandWithTime(modClass, filePathInJar, outputPath, logger);
        return result.success();
    }

    /**
     * 命令触发：执行所有预设的文件操作（用于 /announcement dump 命令）
     *
     * @param modClass 模组主类
     * @param logger 模组的Logger实例
     * @return 是否所有操作都成功
     */
    public static boolean dumpAllResourcesOnCommand(Class<?> modClass, Object logger) {
        ExtractResult result = dumpAllResourcesOnCommandWithTime(modClass, logger);
        return result.success();
    }

    /**
     * 模组初始化时调用：解压指定路径的zip文件到目标目录
     * @deprecated 请使用命令触发的方法
     */
    @Deprecated
    public static boolean extractSpecificZipOnInit(Class<?> modClass, String zipPathInJar, String outputDir, Object logger) {
        return extractSpecificZipOnCommand(modClass, zipPathInJar, outputDir, logger);
    }

    /**
     * 模组初始化时调用：复制jar内的单个文件到指定路径
     * @deprecated 请使用命令触发的方法
     */
    @Deprecated
    public static boolean copyFileFromJar(Class<?> modClass, String filePathInJar, String outputPath, Object logger) {
        return copyFileFromJarOnCommand(modClass, filePathInJar, outputPath, logger);
    }

    /**
     * 模组初始化时调用：移动jar内的单个文件到指定路径
     * @deprecated 请使用命令触发的方法
     */
    @Deprecated
    public static boolean moveFileFromJar(Class<?> modClass, String filePathInJar, String outputPath, Object logger) {
        return moveFileFromJarOnCommand(modClass, filePathInJar, outputPath, logger);
    }
}