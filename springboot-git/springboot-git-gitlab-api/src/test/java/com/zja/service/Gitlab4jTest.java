package com.zja.service;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.GroupParams;
import org.gitlab4j.api.models.Project;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/11/27 16:28
 */
public class Gitlab4jTest {

    private static final String GITLAB_SERVER = "http://elb-791125809.cn-northwest-1.elb.amazonaws.com.cn:5335";
    private static final String GITLAB_ACCESS_TOKEN = "2T5FcVHdGLa";


    @Test
    public void testGetProjects() {
        // Create a GitLabApi instance to communicate with your GitLab server
        try (GitLabApi gitLabApi = new GitLabApi(GITLAB_SERVER, GITLAB_ACCESS_TOKEN)) {

            // Get the list of projects your account has access to
            List<Project> projects = gitLabApi.getProjectApi().getProjects();
            for (Project project : projects) {
                System.out.println(project.getName());
            }
        } catch (GitLabApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateGroup() {
        // Create a GitLabApi instance to communicate with your GitLab server
        try (GitLabApi gitLabApi = new GitLabApi(GITLAB_SERVER, GITLAB_ACCESS_TOKEN)) {

            String groupName = "test002";

            // 创建组
            GroupParams groupParams = new GroupParams();
            groupParams.withName(groupName);
            groupParams.withPath(groupName);

            Group group = gitLabApi.getGroupApi().createGroup(groupParams);
            System.out.println(group);
        } catch (GitLabApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateSubGroup() {
        // Create a GitLabApi instance to communicate with your GitLab server
        try (GitLabApi gitLabApi = new GitLabApi(GITLAB_SERVER, GITLAB_ACCESS_TOKEN)) {

            Long groupParentId = 1844L;
            String groupSubName = "test-sub-02";

            // 创建子组
            GroupParams groupParams = new GroupParams();
            groupParams.withParentId(groupParentId);
            groupParams.withName(groupSubName);
            groupParams.withPath(groupSubName);

            gitLabApi.getGroupApi().createGroup(groupParams);
        } catch (GitLabApiException e) {
            throw new RuntimeException(e);
        }
    }

    // 给组授权用户
    @Test
    public void testAuthorizationGroup() {
        // Create a GitLabApi instance to communicate with your GitLab server
        try (GitLabApi gitLabApi = new GitLabApi(GITLAB_SERVER, GITLAB_ACCESS_TOKEN)) {

            // 给组授权用户
            Long groupId = 1844L;
            Long userId = 10L; // 龙哥
            AccessLevel accessLevel = AccessLevel.MAINTAINER;  // 授权

            gitLabApi.getGroupApi().addMember(groupId, userId, accessLevel);
        } catch (GitLabApiException e) {
            throw new RuntimeException(e);
        }
    }

}
