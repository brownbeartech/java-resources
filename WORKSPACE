load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# Maven
RULES_JVM_EXTERNAL_TAG = "2.3"
RULES_JVM_EXTERNAL_SHA = "375b1592e3f4e0a46e6236e19fc30c8020c438803d4d49b13b40aaacd2703c30"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "org.slf4j:slf4j-api:1.7.22",
    ],
    repositories = [
        "https://jcenter.bintray.com/",
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ]
)