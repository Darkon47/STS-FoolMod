package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.subscribers.OnEvokeOrbSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Keqing extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber, OnEvokeOrbSubscriber
{
    public static final EYBCardData DATA = Register(Keqing.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Piercing);

    private int turns;

    public Keqing()
    {
        super(DATA);

        Initialize(2, 0, 3);
        SetUpgrade(1, 0, 0);
        SetScaling(0, 1, 0);

        SetExhaust(true);
        SetSynergy(Synergies.GenshinImpact);
        SetMartialArtist();
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(3);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        turns = magicNumber;
        CombatStats.onEvokeOrb.Subscribe(this);
        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
        if (cost > 0)
        {
            this.modifyCostForCombat(-1);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL).SetDamageEffect(c -> GameEffects.List.Add(new DieDieDieEffect()));
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        if (IsStarter())
        {
            GameActions.Bottom.Draw(1);
        }
    }

    @Override
    public void OnEvokeOrb(AbstractOrb orb)
    {
        if (Lightning.ORB_ID.equals(orb.ID)) {
            this.reduceTurns();
        }

    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        this.reduceTurns();
    }

    private void reduceTurns()
    {
        if (player.exhaustPile.contains(this))
        {
            if (turns <= 0)
            {
                GameEffects.Queue.ShowCardBriefly(this);
                GameActions.Bottom.MoveCard(this, player.exhaustPile, player.drawPile)
                        .ShowEffect(false, false);
                CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
                CombatStats.onEvokeOrb.Unsubscribe(this);
            }
            else
            {
                turns -= 1;
            }
        }
        else
        {
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            CombatStats.onEvokeOrb.Unsubscribe(this);
        }
    }
}