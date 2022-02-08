package pinacolada.cards.fool.special;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class RinKaenbyou_VengefulSpirit extends FoolCard
{
    public static final PCLCardData DATA = Register(RinKaenbyou_VengefulSpirit.class)
    		.SetCurse(0, PCLCardTarget.None, true)
            .SetSeries(CardSeries.TouhouProject);

    public RinKaenbyou_VengefulSpirit()
    {
        super(DATA, false);

        Initialize(0, 0, 7, 3);
        SetUpgrade(0, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);

        SetAutoplay(true);
        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealDamageToRandomEnemy(magicNumber, DamageInfo.DamageType.HP_LOSS, AttackEffects.DARKNESS);
        PCLActions.Bottom.DealDamageAtEndOfTurn(player, player, secondaryValue, AttackEffects.FIRE);
    }
}
