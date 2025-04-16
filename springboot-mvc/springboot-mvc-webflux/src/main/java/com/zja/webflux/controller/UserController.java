package com.zja.webflux.controller;

import com.zja.webflux.dao.UserRepository;
import com.zja.webflux.model.Page;
import com.zja.webflux.model.User;
import com.zja.webflux.model.UserAdd;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author: zhengja
 * @Date: 2025-04-16 11:21
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 创建用户
    @PostMapping
    public Mono<User> createUser(@RequestBody UserAdd userAdd) {
        User user = new User(null, userAdd.getName(), userAdd.getEmail());
        return userRepository.save(user);
    }

    // 查询所有用户
    @GetMapping("/all")
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 根据ID查询用户
    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // 更新用户
    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>> updateUser(
            @PathVariable Long id,
            @RequestBody User user
    ) {
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setEmail(user.getEmail());
                    return userRepository.save(existingUser);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .flatMap(user -> userRepository.delete(user)
                        .then(Mono.just(ResponseEntity.ok().<Void>build()))
                        .defaultIfEmpty(ResponseEntity.notFound().build()));
    }

    // 分页查询
    @GetMapping
    public Mono<Page<User>> getUsers(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String searchName = "%" + name + "%";
        int offset = page * size;

        Flux<User> userFlux = userRepository.findByNameContaining(searchName, offset, size);
        Mono<Long> countMono = userRepository.countByNameContaining(searchName);

        return Mono.zip(userFlux.collectList(), countMono)
                .map(tuple -> new Page<>(tuple.getT1(), page, size, tuple.getT2()));
    }

    // 文件上传和下载

    // 文件上传
    @PostMapping("/upload")
    public Mono<Void> uploadFile(@RequestPart("file") FilePart filePart) {
        Path path = Paths.get("uploads/" + filePart.filename());
        // 创建父目录
        try {
            Path parentPath = path.getParent();
            if (parentPath != null) {
                java.nio.file.Files.createDirectories(parentPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePart.transferTo(path);
    }

    // 文件下载-直接下载文件
    @GetMapping("/download/{filename}")
    public Mono<ResponseEntity<Resource>> downloadFile(@PathVariable String filename) {
        Path path = Paths.get("uploads/" + filename);
        Resource resource = new FileSystemResource(path);
        return Mono.just(resource)
                .map(r -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(r);
                });
    }


    // 文件下载-读取文件内容
    @GetMapping("/download/v2/{filename}")
    public Mono<Resource> downloadFileV2(@PathVariable String filename) {
        Path path = Paths.get("uploads/" + filename);
        Resource resource = new FileSystemResource(path);
        return Mono.just(resource)
                .doOnNext(r -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Content-Disposition", "attachment; filename=" + filename);
                });
    }


    // 扩展场景： 若需兼容 JPA（如遗留系统迁移），可以通过以下方式混合使用（但不推荐）：
    // 在阻塞代码中使用 @Transactional 和调度器切换
    // @GetMapping("/blocking")
    // public Mono<List<User>> getBlockingUsers() {
    //     return Mono.fromCallable(() -> jpaRepository.findAll())
    //             .subscribeOn(Schedulers.boundedElastic()); // 切换到阻塞线程池
    // }
}
