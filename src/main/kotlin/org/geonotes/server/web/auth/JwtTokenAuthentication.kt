package org.geonotes.server.web.auth

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority


class JwtTokenAuthentication(private val tokenInfo: TokenHandler.TokenInfo?,
                             private var isAuthenticated: Boolean = false) : Authentication {

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.isAuthenticated = isAuthenticated;
    }

    override fun getName(): String? = tokenInfo?.username

    override fun getCredentials(): Any? = tokenInfo?.password

    override fun getPrincipal(): Any? = tokenInfo

    override fun isAuthenticated(): Boolean = isAuthenticated

    override fun getDetails(): Any? = null

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities

    private val authorities = mutableListOf(SimpleGrantedAuthority("user"))
}
