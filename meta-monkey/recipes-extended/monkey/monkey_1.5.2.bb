SUMMARY = "Fast and Lightweight HTTP Server for Linux"
HOMEPAGE = "http://monkey-project.com"
BUGTRACKER = "https://github.com/monkey/monkey/issues"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2ee41112a44fe7014dce33e26468ba93"

SECTION = "net"

SRC_URI = "http://monkey-project.com/releases/1.5/monkey-${PV}.tar.gz \
           file://monkey.service \
           file://monkey.init"

SRC_URI[md5sum] = "c90ccba23a814265c7e0fd3a75f883fd"
SRC_URI[sha256sum] = "2048d49662895f83df475e22d87c45ea8022c0b1565bba222bda2192fd02c349"

EXTRA_OECONF = "--plugdir=${libdir}/monkey/ \
                --logdir=${localstatedir}/log/monkey/ \
                --pidfile=${localstatedir}/run/monkey.pid \
                --default-user=www-data \
                --datadir=${localstatedir}/www/monkey/ \
                --sysconfdir=${sysconfdir}/monkey/ \
                --enable-plugins=* \
                --disable-plugins=polarssl \
                --debug \
                --malloc-libc"

inherit autotools-brokensep pkgconfig update-rc.d systemd

INITSCRIPT_NAME = "monkey"
INITSCRIPT_PARAMS = "defaults 70"

SYSTEMD_SERVICE_${PN} = "monkey.service"

FILES_${PN} += "${localstatedir}/www/monkey/"

CONFFILES_${PN} = "${sysconfdir}/monkey/monkey.conf \
                   ${sysconfdir}/monkey/sites/default \
                   ${sysconfdir}/monkey/monkey.mime \
                   ${sysconfdir}/monkey/plugins.load \
                   ${sysconfdir}/monkey/plugins/proxy_reverse/proxy_reverse.conf \
                   ${sysconfdir}/monkey/plugins/mandril/mandril.conf \
                   ${sysconfdir}/monkey/plugins/fastcgi/fastcgi.conf \
                   ${sysconfdir}/monkey/plugins/logger/logger.conf \
                   ${sysconfdir}/monkey/plugins/cgi/cgi.conf \
                   ${sysconfdir}/monkey/plugins/cheetah/cheetah.conf \
                   ${sysconfdir}/monkey/plugins/dirlisting/dirhtml.conf \
                   ${sysconfdir}/monkey/plugins/dirlisting/themes/guineo/header.theme \
                   ${sysconfdir}/monkey/plugins/dirlisting/themes/guineo/footer.theme \
                   ${sysconfdir}/monkey/plugins/dirlisting/themes/guineo/entry.theme \
                   ${sysconfdir}/monkey/plugins/auth/README \
                   ${sysconfdir}/monkey/plugins/auth/monkey.users \
                   "

do_install_append() {

    mkdir -p ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/monkey.init ${D}${sysconfdir}/init.d/monkey

    if ${@base_contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -d ${D}${systemd_unitdir}/system
        install -m 644 ${WORKDIR}/monkey.service ${D}/${systemd_unitdir}/system
    fi
}
