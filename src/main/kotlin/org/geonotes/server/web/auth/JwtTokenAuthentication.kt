package org.geonotes.server.web.auth

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority


class JwtTokenAuthentication(private val tokenInfo: TokenHandler.TokenInfo?,
                             private var isAuthenticated: Boolean = false) : Authentication {

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.isAuthenticated = isAuthenticated;
    }

    override fun getName(): String? {
        return tokenInfo?.username
    }

    override fun getCredentials(): Any? {
        return tokenInfo?.password
    }

    override fun getPrincipal(): Any? {
        return tokenInfo
    }

    override fun isAuthenticated(): Boolean {
        return isAuthenticated
    }

    override fun getDetails(): Any? {
        return null
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
    }

    private val authorities = mutableListOf(SimpleGrantedAuthority("user"))
}