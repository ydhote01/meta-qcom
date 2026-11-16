PLATFORM = "nord"
PBT_BUILD_DATE = "261104"

require common.inc

SRC_URI[camxlib.sha256sum] = "20669a71e4d9e4df2e326451acf5aa82a5aaee8d60376c3f5e1605f76e24dfc0"
SRC_URI[camx.sha256sum] = "6c8f845b363076b771cbd59252cba4f140fe66d0dfbd15e92e07fd3db648ad83"
SRC_URI[chicdk.sha256sum] = "6cf9213d7b54f70ce0afc30da596a0a831bec7b00c531e3f56b3ce494e604603"
SRC_URI[camxcommon.sha256sum] = "9e96afa52e5b26cf98c581fcf9ceb586343c65c9990ff67de00a32d7413ed690"
SRC_URI[camxtest.sha256sum] = "83932362b17f6ca8a9a61247a40771eb95e6ff9ae21837a69586020b13a7ce35"

DEPENDS += "opencv userspace-resource-manager"

do_install:append() {
    # copy skel file
    install -d ${D}${datadir}/qcom
    cp -r ${S}/usr/share/qcom/nord ${D}${datadir}/qcom/
}
PACKAGE_BEFORE_PN += "${PN}-skel"
RDEPENDS:${PN} += "${PN}-skel"
FILES:${PN}-skel = "${datadir}/qcom"
# Algo librarires are pre-compiled, pre-stripped.
# Skipping QA checks: 'already-stripped', 'arch', 'libdir' because:
# - Library files are Pre-stripped  (already-stripped)
# - skel binaries/library are not AArch64 (arch mismatch)      (arch)
# - Files are installed under /usr/share (non-libdir path) (libdir)
# - .so symlink is used for runtime DSP usage, not a dev artifact (dev-so)
# Skipping QA checks:'ldflags file-rdeps' because:
# - Libraries are prebuilt dsp binary 
INSANE_SKIP:${PN}-skel += " arch libdir already-stripped ldflags file-rdeps"

# Preserve ${PN}-skel naming to avoid ambiguity in package identification.
DEBIAN_NOAUTONAME:${PN}-skel = "1"
# skel is dsp binary not AArch64, so skip file dependency scanning
SKIP_FILEDEPS:camxlib-${CAMX_PLATFORM}-skel = "1"
