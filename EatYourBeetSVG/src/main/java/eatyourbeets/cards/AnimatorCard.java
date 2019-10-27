package eatyourbeets.cards;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.resources.Resources_Animator_Images;
import eatyourbeets.utilities.Utilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patches.AbstractEnums;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static eatyourbeets.cards.AnimatorCard_UltraRare.RENDER_COLOR;

public abstract class AnimatorCard extends EYBCard
{
    protected static final Logger logger = LogManager.getLogger(AnimatorCard.class.getName());

    public static int SynergyReserves;
    public static int SynergiesActivatedThisTurn;

    private static AnimatorCard previousCard = null;
    private static AnimatorCard lastCardPlayed = null;

    private final List<TooltipInfo> customTooltips = new ArrayList<>();
    private Synergy synergy;

    public boolean anySynergy;

    public static String CreateFullID(String cardID)
    {
        return "animator:" + cardID;
    }

    public static void SetLastCardPlayed(AbstractCard card)
    {
        if (SynergyReserves > 0)
        {
            SynergyReserves -= 1;
        }

        if (card == null)
        {
            previousCard = null;
            lastCardPlayed = null;
        }
        else
        {
            previousCard = lastCardPlayed;
            lastCardPlayed = Utilities.SafeCast(card, AnimatorCard.class);
        }
    }

    public boolean HasActiveSynergy()
    {
        if (SynergyReserves > 0)
        {
            return true;
        }
        else if (this == lastCardPlayed)
        {
            return previousCard != null && previousCard.HasSynergy(this);
        }
        else
        {
            return lastCardPlayed != null && lastCardPlayed.HasSynergy(this);
        }
    }

    public boolean HasSynergy(AbstractCard other)
    {
        AnimatorCard card = Utilities.SafeCast(other, AnimatorCard.class);
        if (card != null && card.synergy != null)
        {
            return this.synergy != null && (HasExactSynergy(card.synergy) || this.anySynergy || card.anySynergy);
        }

        return false;
    }

    public boolean HasExactSynergy(Synergy synergy)
    {
        return Objects.equals(this.synergy, synergy);
    }

    public Synergy GetSynergy()
    {
        return synergy;
    }

    @Override
    protected String GetHeaderText()
    {
        if (synergy != null)
        {
            return synergy.NAME;
        }

        return null;
    }

    public void SetSynergy(Synergy synergy)
    {
        SetSynergy(synergy, false);
    }

    public void SetSynergy(Synergy synergy, boolean shapeshifter)
    {
        this.synergy = synergy;
        this.anySynergy = shapeshifter;
    }

    protected AnimatorCard(String id, int cost, CardType type, CardRarity rarity, CardTarget target)
    {
        this(Resources_Animator.GetCardStrings(id), id, Resources_Animator.GetCardImage(id), cost, type, AbstractEnums.Cards.THE_ANIMATOR, rarity, target);
    }

    protected AnimatorCard(String id, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        this(Resources_Animator.GetCardStrings(id), id, Resources_Animator.GetCardImage(id), cost, type, color, rarity, target);
    }

    protected AnimatorCard(CardStrings strings, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(strings, id, imagePath, cost, type, color, rarity, target);

        if (this instanceof AnimatorCard_UltraRare)
        {
            setBannerTexture(Resources_Animator_Images.BANNER_SPECIAL2_PNG, Resources_Animator_Images.BANNER_SPECIAL2_P_PNG);
        }
        else if (rarity == CardRarity.SPECIAL)
        {
            setBannerTexture(Resources_Animator_Images.BANNER_SPECIAL_PNG, Resources_Animator_Images.BANNER_SPECIAL_P_PNG);
        }
    }

    protected void SetLoyal(boolean value)
    {
        if (value)
        {
            if (!tags.contains(AbstractEnums.CardTags.LOYAL))
            {
                tags.add(AbstractEnums.CardTags.LOYAL);
            }
        }
        else
        {
            tags.remove(AbstractEnums.CardTags.LOYAL);
        }
    }
}