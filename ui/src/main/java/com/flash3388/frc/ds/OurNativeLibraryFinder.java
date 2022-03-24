package com.flash3388.frc.ds;

import com.castle.code.NativeLibrary;
import com.castle.code.finder.ArchivedNativeLibrary;
import com.castle.code.finder.NativeLibraryFinder;
import com.castle.exceptions.FindException;
import com.castle.nio.PathMatching;
import com.castle.nio.zip.OpenZip;
import com.castle.nio.zip.Zip;
import com.castle.util.os.Architecture;
import com.castle.util.os.System;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.regex.Pattern;

public class OurNativeLibraryFinder implements NativeLibraryFinder {

    private final Zip mZip;
    private final Path mArchiveBasePath;
    private final Architecture mTargetArchitecture;

    public OurNativeLibraryFinder(Zip zip, Path archiveBasePath, Architecture targetArchitecture) {
        mZip = zip;
        mArchiveBasePath = archiveBasePath;
        mTargetArchitecture = targetArchitecture;
    }

    public OurNativeLibraryFinder(Zip zip, Path archiveBasePath) {
        this(zip, archiveBasePath, System.architecture());
    }

    @Override
    public NativeLibrary find(String name) throws FindException {
        try (OpenZip zip = mZip.open()) {
            Pattern filePattern = buildFindPattern(name);

            Path path = findPath(zip, filePattern);
            return new ArchivedNativeLibrary(name, mTargetArchitecture, mZip, path);
        } catch (IOException e) {
            throw new FindException(e);
        }
    }

    private Pattern buildFindPattern(String name) {
        return Pattern.compile(String.format("^%s\\/%s-%s\\/.*%s\\.(dll|so|dylib)$",
                mArchiveBasePath.toString(),
                mTargetArchitecture.getOperatingSystem().name().toLowerCase(),
                mTargetArchitecture.getArchName(),
                name));
    }

    private Path findPath(OpenZip zip, Pattern pattern) throws FindException, IOException {
        Collection<Path> allFound = zip.pathFinder().findAll(pattern, PathMatching.fileMatcher());
        if (allFound.size() != 1) {
            throw new FindException(String.format("Expected to find 1, by found %d paths: %s",
                    allFound.size(), pattern.pattern()));
        }

        return allFound.iterator().next();
    }
}
