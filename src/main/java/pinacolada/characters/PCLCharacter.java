package pinacolada.characters;

import basemod.abstracts.CustomPlayer;
import basemod.animations.G3DJAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.daily.mods.BlueCards;
import com.megacrit.cardcrawl.daily.mods.GreenCards;
import com.megacrit.cardcrawl.daily.mods.PurpleCards;
import com.megacrit.cardcrawl.daily.mods.RedCards;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.stats.CharStat;
import pinacolada.effects.SFX;
import pinacolada.patches.relicLibrary.RelicLibraryPatches;
import pinacolada.ui.PCLEnergyOrb;
import pinacolada.ui.characterSelection.PCLBaseStatEditor;

import java.util.ArrayList;

public abstract class PCLCharacter extends CustomPlayer
{
    public PCLCharacter(String name, PlayerClass playerClass, PCLEnergyOrb orb)
    {
        super(name, playerClass, orb, (new G3DJAnimation(null, null)));
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
    public Texture getEnergyImage() {
        if (this.energyOrb instanceof PCLEnergyOrb) {
            return ((PCLEnergyOrb)this.energyOrb).getEnergyImage();
        } else {
            return super.getEnergyImage();
        }
    }

    @Override
    public CharStat getCharStat()
    {
        // yes
        return super.getCharStat();
    }
}