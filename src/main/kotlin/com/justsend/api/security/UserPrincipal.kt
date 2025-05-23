package com.justsend.api.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

data class UserPrincipal(
  val id: UUID
) : UserDetails {
  override fun getAuthorities() = emptyList<GrantedAuthority>()
  override fun getPassword() = ""
  override fun getUsername() = id.toString()
  override fun isAccountNonExpired() = true
  override fun isAccountNonLocked() = true
  override fun isCredentialsNonExpired() = true
  override fun isEnabled() = true
}
