package vn.iotstar.service;
import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.dto.*;
import vn.iotstar.entity.*;
import vn.iotstar.mapper.*;
import vn.iotstar.repository.*;
import vn.iotstar.security.AppPrincipal;

@Service public class ManagementService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final ProductRepository products;
    private final UserMapper userMapper;
    private final ProductMapper productMapper;
    private final PasswordEncoder encoder;
    private final ImageService images;
    public ManagementService(UserRepository users, RoleRepository roles, ProductRepository products,
                             UserMapper userMapper, ProductMapper productMapper, PasswordEncoder encoder, ImageService images) {
        this.users=users; this.roles=roles; this.products=products; this.userMapper=userMapper;
        this.productMapper=productMapper; this.encoder=encoder; this.images=images;
    }
    public long userCount() { return users.count(); }
    public long productCount() { return products.count(); }
    public Page<UserDto> users(String keyword, int page) {
        return users.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrFullNameContainingIgnoreCase(
                keyword, keyword, keyword, PageRequest.of(Math.max(0,page),10)).map(user -> {
                    UserDto dto=userMapper.toDto(user); dto.setProductCount(products.countByUserId(user.getId())); return dto;
                });
    }
    public UserForm userForm(Long id) {
        User user=users.findById(id).orElseThrow(); UserForm form=new UserForm();
        form.setUsername(user.getUsername()); form.setEmail(user.getEmail()); form.setFullName(user.getFullName());
        form.setImageUrl(user.getImageUrl()); form.setRoleName(user.getRole().getName()); form.setEnabled(user.isEnabled());
        return form;
    }
    @Transactional public void saveUser(Long id, UserForm form) {
        User user=id==null ? new User() : users.findById(id).orElseThrow();
        if (id==null && (users.existsByUsernameIgnoreCase(form.getUsername()) || users.existsByEmailIgnoreCase(form.getEmail())))
            throw new IllegalArgumentException("Username or email already exists");
        user.setUsername(form.getUsername().trim()); user.setEmail(form.getEmail().trim().toLowerCase());
        user.setFullName(form.getFullName().trim()); user.setImageUrl(form.getImageUrl()); user.setEnabled(form.isEnabled());
        user.setRole(roles.findByName(form.getRoleName()).orElseThrow());
        if (id==null && (form.getPassword()==null || form.getPassword().length()<8))
            throw new IllegalArgumentException("Password must have at least 8 characters");
        if (form.getPassword()!=null && !form.getPassword().isBlank()) user.setPassword(encoder.encode(form.getPassword()));
        users.save(user);
    }
    @Transactional public void deleteUser(Long id) {
        if (products.countByUserId(id)>0) throw new IllegalArgumentException("Delete this user's products first");
        users.deleteById(id);
    }
    public Page<ProductDto> products(String keyword, int page) {
        return products.findByNameContainingIgnoreCase(keyword, PageRequest.of(Math.max(0,page),10)).map(productMapper::toDto);
    }
    public ProductForm productForm(Long id) {
        Product p=products.findById(id).orElseThrow(); ProductForm form=new ProductForm();
        form.setName(p.getName()); form.setDescription(p.getDescription()); form.setPrice(p.getPrice()); return form;
    }
    public ProductForm editableProductForm(Long id, AppPrincipal principal) {
        Product p=products.findById(id).orElseThrow(); assertMayEdit(p, principal);
        return productForm(id);
    }
    @Transactional public void saveProduct(Long id, ProductForm form, MultipartFile image, AppPrincipal principal) throws IOException {
        Product p=id==null ? new Product() : products.findById(id).orElseThrow();
        if (id==null) p.setUser(users.findById(principal.id()).orElseThrow());
        else assertMayEdit(p, principal);
        p.setName(form.getName().trim()); p.setDescription(form.getDescription()); p.setPrice(form.getPrice());
        ImageService.Upload upload=images.upload(image);
        if (upload!=null) { images.delete(p.getImagePublicId()); p.setImageUrl(upload.url()); p.setImagePublicId(upload.publicId()); }
        products.save(p);
    }
    @Transactional public void deleteProduct(Long id, AppPrincipal principal) throws IOException {
        Product p=products.findById(id).orElseThrow(); assertMayEdit(p,principal);
        images.delete(p.getImagePublicId()); products.delete(p);
    }
    private void assertMayEdit(Product p, AppPrincipal principal) {
        if (!"ROLE_ADMIN".equals(principal.role()) && !p.getUser().getId().equals(principal.id()))
            throw new org.springframework.security.access.AccessDeniedException("Not your product");
    }
}
