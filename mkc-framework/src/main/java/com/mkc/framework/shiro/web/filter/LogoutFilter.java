package com.mkc.framework.shiro.web.filter;

import com.mkc.common.constant.Constants;
import com.mkc.common.constant.ShiroConstants;
import com.mkc.common.core.domain.entity.SysUser;
import com.mkc.common.utils.MessageUtils;
import com.mkc.common.utils.ShiroUtils;
import com.mkc.common.utils.StringUtils;
import com.mkc.framework.manager.AsyncManager;
import com.mkc.framework.manager.factory.AsyncFactory;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;

/**
 * 退出过滤器
 * 
 * @author  atd
 */
public class LogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter
{
    private static final Logger log = LoggerFactory.getLogger(LogoutFilter.class);

    /**
     * 退出后重定向的地址
     */
    private String loginUrl;

    private Cache<String, Deque<Serializable>> cache;

    public String getLoginUrl()
    {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl)
    {
        this.loginUrl = loginUrl;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception
    {
        try
        {
            Subject subject = getSubject(request, response);
            String redirectUrl = getRedirectUrl(request, response, subject);
            try
            {
                SysUser user = ShiroUtils.getSysUser();
                if (StringUtils.isNotNull(user))
                {
                    String loginName = user.getLoginName();
                    // 记录用户退出日志
                    AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
                    // 清理缓存
                    removeUserCache(loginName, ShiroUtils.getSessionId());
                }
                // 退出登录
                subject.logout();
            }
            catch (SessionException ise)
            {
                log.error("logout fail.", ise);
            }
            issueRedirect(request, response, redirectUrl);
        }
        catch (Exception e)
        {
            log.error("Encountered session exception during logout.  This can generally safely be ignored.", e);
        }
        return false;
    }

    public void removeUserCache(String loginName, String sessionId)
    {
        Deque<Serializable> deque = cache.get(loginName);
        if (StringUtils.isEmpty(deque) || deque.size() == 0)
        {
            return;
        }
        deque.remove(sessionId);
        cache.put(loginName, deque);
    }

    /**
     * 退出跳转URL
     */
    @Override
    protected String getRedirectUrl(ServletRequest request, ServletResponse response, Subject subject)
    {
        String url = getLoginUrl();
        if (StringUtils.isNotEmpty(url))
        {
            return url;
        }
        return super.getRedirectUrl(request, response, subject);
    }

    // 设置Cache的key的前缀
    public void setCacheManager(CacheManager cacheManager)
    {
        // 必须和ehcache缓存配置中的缓存name一致
        this.cache = cacheManager.getCache(ShiroConstants.SYS_USERCACHE);
    }
}
