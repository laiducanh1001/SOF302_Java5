package com.example.asgm1_java5_version2.controller;

import com.example.asgm1_java5_version2.model.NhanVien;
import com.example.asgm1_java5_version2.repository.NhanVienRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LoginControllerTest {

 @Mock
 private NhanVienRepository repoNV;

 @Mock
 private Model model;

 @Mock
 private HttpSession session;

 @InjectMocks
 private LoginController loginController;

 @BeforeEach
 void setUp() {
  MockitoAnnotations.openMocks(this);
 }

 // Test 1: Đăng nhập thành công
 @Test
 void testLogin_Successful() {
  List<NhanVien> mockNhanVienList = new ArrayList<>();
  mockNhanVienList.add(new NhanVien(1, "Nguyen Van A", "NV001", "user1", "pass1", true));
  when(repoNV.findAll()).thenReturn(mockNhanVienList);

  String view = loginController.login("user1", "pass1", model, session);

  verify(session, times(1)).setAttribute(eq("user"), any(NhanVien.class));
  assertEquals("main", view);
 }

 // Test 2: Đăng nhập với username rỗng
 @Test
 void testLogin_EmptyUsername() {
  String view = loginController.login("", "pass1", model, session);

  verify(model, times(1)).addAttribute(eq("errorMessage"), eq("Khong de trong ten dang nhap"));
  assertEquals("login", view);
 }

 // Test 3: Đăng nhập với password rỗng
 @Test
 void testLogin_EmptyPassword() {
  String view = loginController.login("user1", "", model, session);

  verify(model, times(1)).addAttribute(eq("errorMessage"), eq("Khong de trong mat khau"));
  assertEquals("login", view);
 }

 // Test 4: Đăng nhập thất bại do sai username
 @Test
 void testLogin_InvalidUsername() {
  List<NhanVien> mockNhanVienList = new ArrayList<>();
  mockNhanVienList.add(new NhanVien(1, "Nguyen Van A", "NV001", "user1", "pass1", true));
  when(repoNV.findAll()).thenReturn(mockNhanVienList);

  String view = loginController.login("wrongUser", "pass1", model, session);

  verify(model, times(1)).addAttribute(eq("errorMessage"), eq("Invalid username or password"));
  assertEquals("login", view);
 }

 // Test 5: Đăng nhập thất bại do sai password
 @Test
 void testLogin_InvalidPassword() {
  List<NhanVien> mockNhanVienList = new ArrayList<>();
  mockNhanVienList.add(new NhanVien(1, "Nguyen Van A", "NV001", "user1", "pass1", true));
  when(repoNV.findAll()).thenReturn(mockNhanVienList);

  String view = loginController.login("user1", "wrongPass", model, session);

  verify(model, times(1)).addAttribute(eq("errorMessage"), eq("Invalid username or password"));
  assertEquals("login", view);
 }

 // Test 6: Đăng nhập khi không có nhân viên nào trong hệ thống
 @Test
 void testLogin_NoUsersInSystem() {
  when(repoNV.findAll()).thenReturn(new ArrayList<>());

  String view = loginController.login("user1", "pass1", model, session);

  verify(model, times(1)).addAttribute(eq("errorMessage"), eq("Invalid username or password"));
  assertEquals("login", view);
 }

 // Test 7: Đăng nhập với username và password trống
 @Test
 void testLogin_EmptyUsernameAndPassword() {
  String view = loginController.login("", "", model, session);

  verify(model, times(1)).addAttribute(eq("errorMessage"), eq("Khong de trong ten dang nhap"));
  assertEquals("login", view);
 }
 // Test 8: Đăng nhập với username và password trống
 @Test
 void testLogin_EmptyUsernameAndPassword8() {
  String view = loginController.login("", "123", model, session);

  verify(model, times(1)).addAttribute(eq("errorMessage"), eq("Khong de trong ten dang nhap"));
  assertEquals("login", view);
 }

 // Test 9: Đăng nhập thành công với nhiều tài khoản nhưng chỉ khớp một
 @Test
 void testLogin_SuccessWithMultipleAccounts() {
  List<NhanVien> mockNhanVienList = new ArrayList<>();
  mockNhanVienList.add(new NhanVien(1, "Nguyen Van A", "NV001", "user1", "pass1", true));
  mockNhanVienList.add(new NhanVien(2, "Nguyen Van B", "NV002", "user2", "pass2", true));
  when(repoNV.findAll()).thenReturn(mockNhanVienList);

  String view = loginController.login("user2", "pass2", model, session);

  verify(session, times(1)).setAttribute(eq("user"), any(NhanVien.class));
  assertEquals("main", view);
 }

 // Test 10: Đăng nhập với ký tự đặc biệt trong username
 @Test
 void testLogin_SpecialCharactersInUsername() {
  List<NhanVien> mockNhanVienList = new ArrayList<>();
  mockNhanVienList.add(new NhanVien(1, "Nguyen Van A", "NV001", "user!@#", "pass1", true));
  when(repoNV.findAll()).thenReturn(mockNhanVienList);

  String view = loginController.login("user!@#", "pass1", model, session);

  verify(session, times(1)).setAttribute(eq("user"), any(NhanVien.class));
  assertEquals("main", view);
 }
}