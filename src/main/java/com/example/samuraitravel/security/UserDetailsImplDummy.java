package com.example.samuraitravel.security;

 import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.samuraitravel.entity.User;
 
// public class UserDetailsImpl {
 public class UserDetailsImplDummy implements UserDetails {
     //private final User user;
     //private final Collection<GrantedAuthority> authorities;
	 
	 @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        return null; // 権限は空
	    }
     
     //public UserDetailsImplDummy(User user, Collection<GrantedAuthority> authorities) {
     //    this.user = user;
     //    this.authorities = authorities;
     //}
     
	 public User getUser() {
		 User userDummy= new User();
		 userDummy.setId(-1);
		 userDummy.setEmail("");
		 userDummy.setFurigana("");
		 userDummy.setName("");
		 userDummy.setPassword(null);
		 userDummy.setAddress("");
		 userDummy.setCreatedAt(null);
		 userDummy.setEnabled(true);
		 userDummy.setPhoneNumber(null);
		 userDummy.setPostalCode(null);
		 userDummy.setUpdatedAt(null);
		 userDummy.setRole(null);
         return userDummy;
     }
     
     public Integer getUserId() {
         return -1;
     }
     
     // ハッシュ化済みのパスワードを返す
     @Override
     public String getPassword() {
         return "";
     }
     
     // ログイン時に利用するユーザー名（メールアドレス）を返す
     @Override
     public String getUsername() {
         return"";
     }

    @Override
    public boolean isAccountNonExpired() {
        return true; // アカウントは期限切れではない
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // アカウントはロックされていない
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 資格情報は期限切れではない
    }

    @Override
    public boolean isEnabled() {
        return true; // アカウントは有効
    }

}
