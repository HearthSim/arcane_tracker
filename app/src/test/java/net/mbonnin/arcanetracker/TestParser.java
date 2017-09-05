package net.mbonnin.arcanetracker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.mbonnin.arcanetracker.parser.Entity;
import net.mbonnin.arcanetracker.parser.Game;
import net.mbonnin.arcanetracker.parser.GameLogic;
import net.mbonnin.arcanetracker.parser.PowerParser;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestParser {

    static class SimpleListener implements GameLogic.Listener {
        public Game game;

        @Override
        public void gameStarted(Game game) {
            this.game = game;
        }

        @Override
        public void gameOver() {

        }

        @Override
        public void somethingChanged() {

        }
    }

    @BeforeClass
    public static void beforeClass() {
        Timber.plant(new TestTree());
        InputStream inputStream = TestParser.class.getResourceAsStream("/cards_enUS.json");
        InputStreamReader reader = new InputStreamReader(inputStream);
        ArrayList<Card> list = new Gson().fromJson(reader, new TypeToken<ArrayList<Card>>() {}.getType());
        CardDb.init(list);
    }

    private void runParser(String resource, GameLogic.Listener listener) throws IOException {
        GameLogic.get().addListener(listener);
        PowerParser powerParser = new PowerParser(tag -> GameLogic.get().handleRootTag(tag), null);
        InputStream inputStream = getClass().getResourceAsStream(resource);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        powerParser.onPreviousDataRead();
        while ((line = br.readLine()) != null) {
            powerParser.onLine(line);
        }
        GameLogic.get().removeListener(listener);
    }

    private Game runParser(String resource) throws IOException {
        SimpleListener listener = new SimpleListener();

        runParser(resource, listener);
        return listener.game;
    }

    @Test
    public void testCreatedBy() throws Exception {
        Game game = runParser("/gRievoUS.log");

        Assert.assertEquals(game.findEntitySafe("73").extra.createdBy, Card.SERVANT_OF_KALYMOS);
        Assert.assertEquals(game.findEntitySafe("78").extra.createdBy, Card.SERVANT_OF_KALYMOS);
        Assert.assertEquals(game.findEntitySafe("75").extra.createdBy, Card.FROZEN_CLONE);
        Assert.assertEquals(game.findEntitySafe("76").extra.createdBy, Card.FROZEN_CLONE);
        Assert.assertEquals(game.findEntitySafe("80").extra.createdBy, Card.MIRROR_ENTITY);
    }

    @Test
    public void testDoubleSecret() throws Exception {

        Game game = runParser("/k0l0banov.log");

        Assert.assertFalse(game.victory);

    }

    @Test
    public void testSecrets() throws Exception {

        runParser("/Laugeolesen.log", new SimpleListener() {
            @Override
            public void somethingChanged() {
                if ("19".equals(game.gameEntity.tags.get(Entity.KEY_TURN))) {
                    Entity secretEntity = game.findEntityUnsafe("84");
                    Assert.assertFalse(secretEntity.extra.competitiveSpiritTriggerConditionHappened);
                    Assert.assertTrue(secretEntity.extra.otherPlayerHeroPowered);
                    Assert.assertTrue(secretEntity.extra.otherPlayerPlayedMinion);
                    Assert.assertTrue(secretEntity.extra.selfHeroDamaged);
                    Assert.assertTrue(secretEntity.extra.selfHeroAttacked);
                }
            }
        });
    }
    
    @Test
    public void testMirrorEntity() throws Exception {
        runParser("/MightyElf.log", new SimpleListener() {
            @Override
            public void somethingChanged() {
                Entity kabalCrystalRunner = game.findEntitySafe("84");
                if (Entity.ZONE_PLAY.equals(kabalCrystalRunner.tags.get(Entity.KEY_ZONE))) {
                    Entity iceBarrier = game.findEntitySafe("41");
                    Assert.assertTrue(iceBarrier.extra.otherPlayerPlayedMinion);
                }
            }
        });
    }

    @Test
    public void testFrozenClone() throws Exception {
        runParser("/MightyElf.log", new SimpleListener() {
            @Override
            public void somethingChanged() {
                Entity kabalCrystalRunner = game.findEntitySafe("84");
                if (Entity.ZONE_PLAY.equals(kabalCrystalRunner.tags.get(Entity.KEY_ZONE))) {
                    Entity iceBarrier = game.findEntitySafe("41");
                    Assert.assertTrue(iceBarrier.extra.otherPlayerPlayedMinion);
                }
            }
        });
    }

    @Test
    public void testExploreUngoro() throws Exception {
        runParser("/exploreUngoro.log", new SimpleListener() {
            @Override
            public void somethingChanged() {
                Entity e = game.findEntityUnsafe("99");
                if (e != null) {
                    Assert.assertTrue(e.CardID.equals(Card.CHOOSE_YOUR_PATH));
                }
            }
        });
    }

    @Test
    public void testInterrupted() throws Exception {
        class InterruptedListener implements  GameLogic.Listener {
            public int gameOverCount;

            @Override
            public void gameStarted(Game game) {

            }

            @Override
            public void gameOver() {
                gameOverCount++;
            }

            @Override
            public void somethingChanged() {

            }
        }

        InterruptedListener listener = new InterruptedListener();
        runParser("/interrupted.log", listener);

        Assert.assertTrue(listener.gameOverCount == 1);
    }
}