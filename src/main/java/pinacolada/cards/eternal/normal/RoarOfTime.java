package pinacolada.cards.eternal.normal;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class RoarOfTime extends EternalCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(RoarOfTime.class).SetSkill(2, CardRarity.UNCOMMON, PCLCardTarget.Normal);
    protected int hpLoss;
    protected AbstractMonster hpLossTarget;

    public RoarOfTime()
    {
        super(DATA);

        Initialize(0, 0, 3, 10);
        SetUpgrade(0, 0, 0, 3);

        SetLight();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Scry(magicNumber).AddCallback(cards -> {
            hpLoss = PCLJUtils.SumInt(cards, c -> c.type == CardType.ATTACK ? secondaryValue : 0);
           if (hpLoss > 0) {
               RoarOfTime other = (RoarOfTime) makeStatEquivalentCopy();
               other.hpLoss = hpLoss;
               other.hpLossTarget = hpLossTarget;
               PCLCombatStats.onStartOfTurnPostDraw.Subscribe(other);
           }
        });
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        super.OnStartOfTurnPostDraw();
        PCLGameEffects.Queue.ShowCardBriefly(this);
        PCLActions.Bottom.SFX(SFX.MONSTER_SNECKO_GLARE);
        PCLActions.Bottom.VFX(VFX.Intimidate(player.hb), 0.1f);
        PCLActions.Bottom.VFX(VFX.ShockWave(player.hb, Color.PURPLE), 0.1f);

        if (hpLossTarget != null && !PCLGameUtilities.IsDeadOrEscaped(hpLossTarget)) {
            PCLActions.Bottom.DealDamage(player, hpLossTarget, hpLoss, DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.NONE);
        }

        PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
    }
}