package vn.iotstar.service;
import com.cloudinary.Cloudinary;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service public class ImageService {
    private final Cloudinary cloudinary;
    private final boolean configured;
    public ImageService(@Value("${app.cloudinary.cloud-name:}") String cloudName,
                        @Value("${app.cloudinary.api-key:}") String apiKey,
                        @Value("${app.cloudinary.api-secret:}") String apiSecret) {
        configured = !cloudName.isBlank() && !apiKey.isBlank() && !apiSecret.isBlank();
        cloudinary = configured ? new Cloudinary(Map.of("cloud_name", cloudName, "api_key", apiKey,
                "api_secret", apiSecret, "secure", true)) : null;
    }
    public record Upload(String url, String publicId) {}
    public Upload upload(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) return null;
        if (!configured) throw new IllegalStateException("Cloudinary is not configured");
        if (image.getSize() > 5_000_000 || image.getContentType() == null ||
                !image.getContentType().startsWith("image/")) throw new IllegalArgumentException("Invalid image");
        Map<?, ?> result = cloudinary.uploader().upload(image.getBytes(), Map.of("folder", "security-vd123"));
        return new Upload(String.valueOf(result.get("secure_url")), String.valueOf(result.get("public_id")));
    }
    public void delete(String publicId) throws IOException {
        if (configured && publicId != null && !publicId.isBlank()) cloudinary.uploader().destroy(publicId, Map.of());
    }
}
