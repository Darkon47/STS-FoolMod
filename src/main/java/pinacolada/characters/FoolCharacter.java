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
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.fool.basic.Strike;
import pinacolada.effects.SFX;
import pinacolada.patches.relicLibrary.RelicLibraryPatches;
import pinacolada.resources.PGR;
import pinacolada.resources.pcl.PCLResources;
import pinacolada.resources.pcl.misc.PCLLoadout;
import pinacolada.ui.PCLEnergyOrb;
import pinacolada.ui.characterSelection.PCLBaseStatEditor;

import java.util.ArrayList;

public class FoolCharacter extends PCLCharacter
{
    public static final TextureCache[] ORB_TEXTURES =
            {
                    PGR.Fool.Images.ORB_BASE_LAYER,
                    PGR.Fool.Images.ORB_TOP_LAYER1,
                    PGR.Fool.Images.ORB_TOP_LAYER2,
                    PGR.Fool.Images.ORB_TOP_LAYER3,
                    PGR.Fool.Images.ORB_TOP_LAYER4,
            };
    public static final CharacterStrings characterStrings = PCLResources.GetCharacterStrings("Fool");
    public static final Color MAIN_COLOR = CardHelper.getColor(210, 147, 106);
    public static final String[] NAMES = characterStrings.NAMES;
    public static final String[] TEXT = characterStrings.TEXT;
    public static final String ORIGINAL_NAME = NAMES[0];
    public static final String OVERRIDE_NAME = NAMES.length > 1 ? NAMES[1] : ORIGINAL_NAME; // Support for Beta/Alt

    public FoolCharacter()
    {
        super(ORIGINAL_NAME, PGR.Fool.PlayerClass, new PCLEnergyOrb(ORB_TEXTURES, PGR.Fool.Images.ORB_FLASH));

        initializeClass(null, PGR.Fool.Images.SHOULDER2_PNG, PGR.Fool.Images.SHOULDER1_PNG, PGR.Fool.Images.CORPSE_PNG,
        getLoadout(), 0f, -5f, 240f, 244f, new EnergyManager(3));

        reloadAnimation();
    }

    public void reloadAnimation()
    {
        this.loadAnimation(PGR.Fool.Images.SKELETON_ATLAS, PGR.Fool.Images.SKELETON_JSON, 1f);
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
    public String getTitle(AbstractPlayer.PlayerClass playerClass) // Top panel title
    {
        return OVERRIDE_NAME;
    }

    @Override
    public AbstractPlayer newInstance()
    {
        return new FoolCharacter();
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
        return Color.SKY;
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
        return FontHelper.energyNumFontBlue;
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
        return PrepareLoadout().GetStartingDeck();
    }

    @Override
    public ArrayList<String> getStartingRelics()
    {
        return PrepareLoadout().GetStartingRelics();
    }

    @Override
    public AbstractCard getStartCardForEvent()
    {
        return new Strike();
    }

    @Override
    public CharSelectInfo getLoadout()
    {
        return PrepareLoadout().GetLoadout(NAMES[0], TEXT[0], this);
    }

    @Override
    public AbstractCard.CardColor getCardColor()
    {
        return PGR.Fool.CardColor;
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

    protected PCLLoadout PrepareLoadout()
    {
        int unlockLevel = PGR.Fool.GetUnlockLevel();
        if (unlockLevel < PGR.Fool.Data.SelectedLoadout.UnlockLevel)
        {
            RandomizedList<PCLLoadout> list = new RandomizedList<>();
            for (PCLLoadout loadout : PGR.Fool.Data.BaseLoadouts)
            {
                if (unlockLevel >= loadout.UnlockLevel)
                {
                    list.Add(loadout);
                }
            }

            PGR.Fool.Data.SelectedLoadout = list.Retrieve(new com.megacrit.cardcrawl.random.Random());
        }

        return PGR.Fool.Data.SelectedLoadout;
    }
}