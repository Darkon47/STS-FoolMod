package pinacolada.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.daily.mods.BlueCards;
import com.megacrit.cardcrawl.daily.mods.GreenCards;
import com.megacrit.cardcrawl.daily.mods.PurpleCards;
import com.megacrit.cardcrawl.daily.mods.RedCards;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.screens.stats.CharStat;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.effects.SFX;
import pinacolada.patches.relicLibrary.RelicLibraryPatches;
import pinacolada.relics.eternal.MusouNoHitotachi;
import pinacolada.resources.PGR;
import pinacolada.resources.pcl.PCLResources;
import pinacolada.ui.PCLEnergyOrb;
import pinacolada.ui.characterSelection.PCLBaseStatEditor;

import java.util.ArrayList;

public class EternalCharacter extends PCLCharacter
{
    public static final CharacterStrings characterStrings = PCLResources.GetCharacterStrings("Eternal");
    public static final Color MAIN_COLOR = CardHelper.getColor(157, 76, 210);
    public static final String[] NAMES = characterStrings.NAMES;
    public static final String[] TEXT = characterStrings.TEXT;
    public static final String ORIGINAL_NAME = NAMES[0];
    public static final String OVERRIDE_NAME = NAMES.length > 1 ? NAMES[1] : ORIGINAL_NAME; // Support for Beta/Alt
    public static final PCLAffinity[] AVAILABLE_AFFINITIES = new PCLAffinity[] {PCLAffinity.Light, PCLAffinity.Dark, PCLAffinity.Silver, PCLAffinity.Star};

    public EternalCharacter()
    {
        super(ORIGINAL_NAME, PGR.Eternal.PlayerClass, new PCLEnergyOrb());

        initializeClass(null, PGR.Eternal.Images.SHOULDER2_PNG, PGR.Eternal.Images.SHOULDER1_PNG, PGR.Eternal.Images.CORPSE_PNG,
        getLoadout(), 0f, -5f, 240f, 244f, new EnergyManager(3));

        reloadAnimation();
    }

    public void reloadAnimation()
    {
        this.loadAnimation(PGR.Eternal.Images.SKELETON_ATLAS, PGR.Eternal.Images.SKELETON_JSON, 1f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        this.stateData.setMix("Hit", "Idle", 0.1f);
        e.setTimeScale(0.9f);
    }

    @Override
    public void damage(DamageInfo info)
    {
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output - this.currentBlock > 0)
        {
            AnimationState.TrackEntry e = this.state.setAnimation(0, "Hit", false);
            this.state.addAnimation(0, "Idle", true, 0f);
            e.setTimeScale(0.9f);
        }

        super.damage(info);
    }

    @Override
    public String getLocalizedCharacterName()
    {
        return ORIGINAL_NAME;
    }

    @Override
    public String getTitle(PlayerClass playerClass) // Top panel title
    {
        return OVERRIDE_NAME;
    }

    @Override
    public AbstractPlayer newInstance()
    {
        return new EternalCharacter();
    }

    @Override
    public ArrayList<String> getRelicNames()
    {
        final ArrayList<String> list = new ArrayList<>();
        for (AbstractRelic r : relics)
        {
            RelicLibraryPatches.AddRelic(list, r);
        }

        return list;
    }

    @Override
    public ArrayList<AbstractCard> getCardPool(ArrayList<AbstractCard> arrayList)
    {
        arrayList = super.getCardPool(arrayList);

        if (ModHelper.isModEnabled(RedCards.ID))
        {
            CardLibrary.addRedCards(arrayList);
        }
        if (ModHelper.isModEnabled(GreenCards.ID))
        {
            CardLibrary.addGreenCards(arrayList);
        }
        if (ModHelper.isModEnabled(BlueCards.ID))
        {
            CardLibrary.addBlueCards(arrayList);
        }
        if (ModHelper.isModEnabled(PurpleCards.ID))
        {
            CardLibrary.addPurpleCards(arrayList);
        }

        return arrayList;
    }

    @Override
    public String getSpireHeartText()
    {
        return com.megacrit.cardcrawl.events.beyond.SpireHeart.DESCRIPTIONS[10];
    }

    @Override
    public Color getSlashAttackColor()
    {
        return Color.PURPLE;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect()
    {
        return new AbstractGameAction.AttackEffect[]
        {
            AbstractGameAction.AttackEffect.SLASH_HEAVY,
            AbstractGameAction.AttackEffect.FIRE,
            AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
            AbstractGameAction.AttackEffect.SLASH_HEAVY,
            AbstractGameAction.AttackEffect.FIRE,
            AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        };
    }

    @Override
    public String getVampireText()
    {
        return com.megacrit.cardcrawl.events.city.Vampires.DESCRIPTIONS[5];
    }

    @Override
    public Color getCardTrailColor()
    {
        return MAIN_COLOR.cpy();
    }

    @Override
    public int getAscensionMaxHPLoss()
    {
        return PCLBaseStatEditor.StatType.HP.BaseAmount / 10;
    }

    @Override
    public BitmapFont getEnergyNumFont()
    {
        return FontHelper.energyNumFontPurple;
    }

    @Override
    public void doCharSelectScreenSelectEffect()
    {
        CardCrawlGame.sound.playA(getCustomModeCharacterButtonSoundKey(), MathUtils.random(-0.1f, 0.2f));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey()
    {
        return SFX.TINGSHA;
    }

    @Override
    public String getPortraitImageName()
    {
        return null; // Updated in AnimatorCharacterSelectScreen
    }

    @Override
    public ArrayList<String> getStartingDeck()
    {
        ArrayList<String> cards = new ArrayList();
        cards.add(pinacolada.cards.eternal.basic.Strike.DATA.ID);
        cards.add(pinacolada.cards.eternal.basic.Strike.DATA.ID);
        cards.add(pinacolada.cards.eternal.basic.Strike.DATA.ID);
        cards.add(pinacolada.cards.eternal.basic.Defend.DATA.ID);
        cards.add(pinacolada.cards.eternal.basic.Defend.DATA.ID);
        cards.add(pinacolada.cards.eternal.basic.Defend.DATA.ID);
        cards.add(pinacolada.cards.eternal.basic.ImprovedStrike.DATA.ID);
        cards.add(pinacolada.cards.eternal.basic.ImprovedStrike.DATA.ID);
        cards.add(pinacolada.cards.eternal.normal.Origin.DATA.ID);
        cards.add(pinacolada.cards.eternal.normal.OpportuneGift.DATA.ID);
        return cards;
    }

    @Override
    public ArrayList<String> getStartingRelics()
    {
        ArrayList<String> relics = new ArrayList();
        relics.add(MusouNoHitotachi.ID);
        return relics;
    }

    @Override
    public AbstractCard getStartCardForEvent()
    {
        return new pinacolada.cards.eternal.basic.Strike();
    }

    @Override
    public CharSelectInfo getLoadout()
    {
        return new CharSelectInfo(NAMES[0], TEXT[0], 70, 70, 1, 70, 5, this, this.getStartingRelics(), this.getStartingDeck(), false);
    }

    @Override
    public AbstractCard.CardColor getCardColor()
    {
        return PGR.Eternal.CardColor;
    }

    @Override
    public Color getCardRenderColor()
    {
        return MAIN_COLOR.cpy();
    }

    @Override
    public CharStat getCharStat()
    {
        // yes
        return super.getCharStat();
    }
}