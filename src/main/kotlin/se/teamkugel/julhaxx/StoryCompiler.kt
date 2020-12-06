package se.teamkugel.julhaxx

import com.samskivert.mustache.Mustache
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.util.FileCopyUtils
import java.io.IOException
import java.io.InputStreamReader
import java.io.UncheckedIOException
import java.nio.charset.StandardCharsets.UTF_8


@Configuration
class StoryCompiler {

    private val resourceLoader: ResourceLoader = DefaultResourceLoader()

    val daysToStoryHtml = (0..7).map {
        it to Mustache.compiler().compile(readFileToString("classpath:templates/story/day${it}.mustache")).execute(null)
    }.toMap()


    fun resourceAsString(resource: Resource): String {
        try {
            InputStreamReader(resource.inputStream, UTF_8).use { reader -> return FileCopyUtils.copyToString(reader)
            }
        } catch (e: IOException) {
            throw UncheckedIOException(e)
        }
    }

    fun readFileToString(path: String): String {
        val resource = resourceLoader.getResource(path)
        return resourceAsString(resource)
    }

}

