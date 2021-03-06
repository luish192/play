package play.i18n;

import org.junit.Test;
import play.Play;
import play.PlayBuilder;
import play.mvc.Http;
import play.test.FunctionalTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.fest.assertions.Assertions.*;

public class LangTest {

    @Test
    public void testChange() {
        new PlayBuilder().build();
        Play.langs = Arrays.asList("no", "en", "fr");
        Lang.current.set(null);
        assertThat(Lang.current.get()).isNull();

        Lang.change("no");
        assertThat(Lang.current.get()).isEqualTo("no");
        Lang.change("nox");
        assertThat(Lang.current.get()).isEqualTo("no");
        Lang.change("EN");
        assertThat(Lang.current.get()).isEqualTo("en");
        Lang.change("fr");
        assertThat(Lang.current.get()).isEqualTo("fr");

        Lang.change("xx");
        assertThat(Lang.current.get()).isEqualTo("fr");

        Lang.change("en_uk");
        assertThat(Lang.current.get()).isEqualTo("en");

        Play.langs = Arrays.asList("no", "en", "en_uk", "fr");
        Lang.current.set(null);
        Lang.change("en_uk");
        assertThat(Lang.current.get()).isEqualTo("en_uk");
        Lang.change("en");
        assertThat(Lang.current.get()).isEqualTo("en");
        Lang.change("en_qw");
        assertThat(Lang.current.get()).isEqualTo("en");
    }

    @Test
    public void testGet() {
        new PlayBuilder().build();
        Play.langs = Arrays.asList("no", "en", "en_uk", "fr");
        Lang.current.set(null);

        Http.Response.current.set( new Http.Response());

        // check default when missing request
        Http.Request.current.set(null);
        assertThat(Lang.get()).isEqualTo("no");

        // check default when missing info in request
        Http.Request req = FunctionalTest.newRequest();
        Http.Request.current.set(req);
        Lang.current.set(null);
        assertThat(Lang.get()).isEqualTo("no");

        // check only with accept-language,  without cookie value
        req = FunctionalTest.newRequest();
        req.headers.put("accept-language", new Http.Header("accept-language", "x"));
        Http.Request.current.set(req);
        Lang.current.set(null);
        assertThat(Lang.get()).isEqualTo("no");

        req = FunctionalTest.newRequest();
        req.headers.put("accept-language", new Http.Header("accept-language", "no"));
        Http.Request.current.set(req);
        Lang.current.set(null);
        assertThat(Lang.get()).isEqualTo("no");

        req = FunctionalTest.newRequest();
        req.headers.put("accept-language", new Http.Header("accept-language", "en"));
        Http.Request.current.set(req);
        Lang.current.set(null);
        assertThat(Lang.get()).isEqualTo("en");

        req = FunctionalTest.newRequest();
        req.headers.put("accept-language", new Http.Header("accept-language", "x,en"));
        Http.Request.current.set(req);
        Lang.current.set(null);
        assertThat(Lang.get()).isEqualTo("en");

        req = FunctionalTest.newRequest();
        req.headers.put("accept-language", new Http.Header("accept-language", "en_uk"));
        Http.Request.current.set(req);
        Lang.current.set(null);
        assertThat(Lang.get()).isEqualTo("en_uk");

        req = FunctionalTest.newRequest();
        req.headers.put("accept-language", new Http.Header("accept-language", "x,en_uk"));
        Http.Request.current.set(req);
        Lang.current.set(null);
        assertThat(Lang.get()).isEqualTo("en_uk");

        // check with cookie value

        req = FunctionalTest.newRequest();

        Http.Cookie cookie = new Http.Cookie();
        cookie.name = "PLAY_LANG";
        cookie.value = "x";//not found in cookie
        req.cookies.put(cookie.name, cookie);
        req.headers.put("accept-language", new Http.Header("accept-language", "en"));
        Http.Request.current.set(req);
        Lang.current.set(null);
        assertThat(Lang.get()).isEqualTo("en");

        cookie = new Http.Cookie();
        cookie.name = "PLAY_LANG";
        cookie.value = "en";
        req.cookies.put(cookie.name, cookie);
        Http.Request.current.set(req);
        Lang.current.set(null);
        assertThat(Lang.get()).isEqualTo("en");

        cookie = new Http.Cookie();
        cookie.name = "PLAY_LANG";
        cookie.value = "en_q";
        req.cookies.put(cookie.name, cookie);
        Http.Request.current.set(req);
        Lang.current.set(null);
        assertThat(Lang.get()).isEqualTo("en");

        cookie = new Http.Cookie();
        cookie.name = "PLAY_LANG";
        cookie.value = "en_uk";
        req.cookies.put(cookie.name, cookie);
        Http.Request.current.set(req);
        Lang.current.set(null);
        assertThat(Lang.get()).isEqualTo("en_uk");


    }
}
