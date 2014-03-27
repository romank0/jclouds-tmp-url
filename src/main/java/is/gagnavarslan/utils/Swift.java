package is.gagnavarslan.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.http.HttpRequest;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

public class Swift {

	public static void main(String[] args) throws FileNotFoundException {

		try {
			String provider = "swift-keystone";
	
			Iterable<Module> modules = ImmutableSet
					.<Module> of(new SLF4JLoggingModule());
	
			Properties properties = new Properties();
			properties.load(Swift.class.getResourceAsStream("/swift.properties"));
			BlobStoreContext context = ContextBuilder.newBuilder(provider)
					.modules(modules).overrides(properties)
					.build(BlobStoreContext.class);
			BlobStore blobStore = context.getBlobStore();
	
			HttpRequest request = blobStore
					.getContext()
					.getSigner()
					.signGetBlob(properties.getProperty("container"),
							properties.getProperty("blob_id"),
							Long.parseLong(properties.getProperty("timeout")));
			
			System.out.println("temp URL=" + request.getEndpoint());

			long sleepInterval = Long.parseLong(properties.getProperty("sleep"));
			System.out.print(String.format("sleeping for %d seconds", sleepInterval));
			Thread.sleep(sleepInterval * 1000L);
			
			request = blobStore
					.getContext()
					.getSigner()
					.signGetBlob(properties.getProperty("container"),
							properties.getProperty("blob_id"),
							Long.parseLong(properties.getProperty("timeout")));
			
			System.out.println("temp URL=" + request.getEndpoint());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			
		}
	}
}
