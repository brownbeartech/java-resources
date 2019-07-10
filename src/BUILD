package(default_visibility = ["//visibility:public"])

load("@rules_jvm_external//:defs.bzl", "artifact")

java_library(
    name = "resources",
    srcs = glob(["tech/brownbear/resources/*.java"]),
    deps = [
        "@maven//:org_slf4j_slf4j_api",
    ]
)