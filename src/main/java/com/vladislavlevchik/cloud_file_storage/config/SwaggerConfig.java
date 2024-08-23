package com.vladislavlevchik.cloud_file_storage.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Cloudify API",
                version = "1.0",
                description = "API for cloud file storage"
        )
)
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch("/api/v1/users/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.getPaths().values().forEach(pathItem -> {
                        pathItem.readOperations().forEach(operation -> {
                            if (operation.getOperationId().equals("register")) {
                                operation.getResponses().remove("401");
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("413");
                            }
                        });
                    });
                })
                .build();
    }

    @Bean
    public GroupedOpenApi fileApi() {
        return GroupedOpenApi.builder()
                .group("file")
                .pathsToMatch("/api/v1/files/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.getPaths().values().forEach(pathItem -> {
                        pathItem.readOperations().forEach(operation -> {
                            if (operation.getOperationId().equals("userMemoryInfo")) {
                                operation.getResponses().remove("400");
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }

                            if (operation.getOperationId().equals("listFilesInAllFilesFolder")) {
                                operation.getResponses().remove("400");
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("listFilesInDeleteFolder")) {
                                operation.getResponses().remove("400");
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("listFilesAndDirectories")) {
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("deleteFiles")) {
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("moveFiles")) {
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("moveFilesToDeleted")) {
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("moveFilesToPackage")) {
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("recoversFiles")) {
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("renameFile")) {
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("searchFileInAllFiles")) {
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("searchFileInDeletedFiles")) {
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("downloadFile")) {
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                        });
                    });
                })
                .build();
    }

    @Bean
    public GroupedOpenApi folderApi() {
        return GroupedOpenApi.builder()
                .group("folder")
                .pathsToMatch("/api/v1/folders/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.getPaths().values().forEach(pathItem -> {
                        pathItem.readOperations().forEach(operation -> {
                            if (operation.getOperationId().equals("createFolder")) {
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("listFolders")) {
                                operation.getResponses().remove("400");
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("listFoldersForMove")) {
                                operation.getResponses().remove("400");
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("renameFolder")) {
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("changeColor")) {
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("deleteFolder")) {
                                operation.getResponses().remove("400");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                        });
                    });
                })
                .build();
    }

    @Bean
    public GroupedOpenApi subfolderApi() {
        return GroupedOpenApi.builder()
                .group("subfolder")
                .pathsToMatch("/api/v1/subfolders/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.getPaths().values().forEach(pathItem -> {
                        pathItem.readOperations().forEach(operation -> {
                            if (operation.getOperationId().equals("createSubFolder")) {
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("renameSubfolder")) {
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("deleteSubfolder")) {
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                        });
                    });
                })
                .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch("/api/v1/auth/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.getPaths().values().forEach(pathItem -> {
                        pathItem.readOperations().forEach(operation -> {
                            if (operation.getOperationId().equals("login")) {
                                operation.getResponses().remove("401");
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                            if (operation.getOperationId().equals("getAuthStatus")) {
                                operation.getResponses().remove("400");
                                operation.getResponses().remove("404");
                                operation.getResponses().remove("409");
                                operation.getResponses().remove("413");
                            }
                        });
                    });
                })
                .build();
    }


}
