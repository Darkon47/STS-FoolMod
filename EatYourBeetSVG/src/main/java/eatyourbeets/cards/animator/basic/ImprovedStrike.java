package eatyourbeets.cards.animator.basic;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public abstract class ImprovedStrike extends ImprovedBasicCard
{
    public static final ArrayList<EYBCardData> list = new ArrayList<>();

    public static ArrayList<EYBCardData> GetCards()
    {
        if (list.isEmpty())
        {
            list.add(Strike_Red.DATA);
            list.add(Strike_Green.DATA);
            list.add(Strike_Blue.DATA);
            list.add(Strike_Light.DATA);
            list.add(Strike_Dark.DATA);
            list.add(Strike_Star.DATA);
        }

        return list;
    }

    protected static EYBCardData Register(Class<? extends AnimatorCard> type)
    {
        return AnimatorCard.Register(type)
                .SetColor(CardColor.COLORLESS)
                .SetAttack(1, CardRarity.BASIC)
                .SetImagePath(GR.GetCardImage(Strike.DATA.ID + "Alt1"));
    }

    public ImprovedStrike(EYBCardData data, AffinityType type)
    {
        super(data, type);

        if (affinityType == AffinityType.Star)
        {
            Initialize(5, 0, 3);
        }
        else
        {
            Initialize(6, 0, 2);
        }
        SetUpgrade(0, 3);
        SetUpgrade(3, 0);

        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(GR.Enums.CardTags.IMPROVED_STRIKE);
    }

    @Override
    protected Texture GetPortraitForeground()
    {
        return GR.GetTexture(GR.GetCardImage(Strike.DATA.ID + "Alt2"), true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        if (upgraded)
        {
            SecondaryEffect();
        }
    }
}