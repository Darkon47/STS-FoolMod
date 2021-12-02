package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.GirlDeMo;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Yui extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yui.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None, true).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new GirlDeMo(), false));

    public Yui()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(2, 0, 1);
        SetHarmonic(true);
        SetExhaust(true);
        SetCostUpgrade(-1);
        AfterLifeMod.Add(this);

        SetAffinityRequirement(Affinity.General, 8);
    }

    @Override
    public AbstractAttribute GetSecondaryInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.MakeCardInHand(AffinityToken.GetCard(Affinity.Light)).SetUpgrade(upgraded, false).AddCallback(
                () -> {
                    GameActions.Bottom.Motivate(secondaryValue);
                }
        );

        if (CheckAffinity(Affinity.General) && CombatStats.ControlPile.Contains(this))
        {
            GameActions.Bottom.MakeCardInDrawPile(new GirlDeMo());
        }
    }
}